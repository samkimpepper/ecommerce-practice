package com.example.ecommerce.product

import com.example.ecommerce.product.document.Product
import com.example.ecommerce.product.document.ProductOption
import com.example.ecommerce.product.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
        private val productRepository: ProductRepository,
        private val productOptionRepository: ProductOptionRepository,
) {
    private val pageSize: Int = 8

    fun saveProduct(request: SaveProductRequest, merchantId: String): Mono<Product> {
        val product = Product(
                name = request.name,
                image = request.image,
                description = request.description,
                merchantId = merchantId,
                shippingCost = request.shippingCost,
        )

        return productRepository.save(product)
    }

    @Transactional
    fun saveProductOption(request: SaveProductOptionRequest): Mono<ProductOption> {
        val productMono = productRepository.findById(request.productId)
            .flatMap { product ->
                product.increaseStock(request.stock)
                if (product.minimumPrice > request.price) {
                    product.minimumPrice = request.price
                }
                productRepository.save(product)
            }

        val optionMono = Mono.just(
            ProductOption(
                productId = request.productId,
                price = request.price,
                size = request.size,
                stock = request.stock,
            )
        ).flatMap { option ->
            productOptionRepository.save(option)
        }

        return Mono.zip(productMono, optionMono) { _, option ->
            option
        }
        .switchIfEmpty(Mono.error<ProductOption>(Exception("Product not found")))
    }

    fun getProductInfo(productId: String): Mono<ProductInfoResponse> {
        return productRepository.findById(productId)
                .flatMap { product ->
                    productOptionRepository.findAllByProductId(productId)
                            .collectList()
                            .map { options ->
                                ProductOptionInfo.fromProductOptions(options)
                            }
                            .map { optionInfos ->
                                ProductInfoResponse.fromProduct(product).copy(productOptions = optionInfos)
                            }
                }
    }

    fun restock(restockRequest: RestockRequest): Mono<ProductInfoResponse> {
        return productRepository.findById(restockRequest.productId)
            .switchIfEmpty(Mono.error(Exception("product not found")))
                .flatMap { product ->
                    val totalStock = restockRequest.optionStockUpdates.values.sum()
                    product.increaseStock(totalStock)
                    productRepository.save(product)
                            .thenMany(Flux.fromIterable(restockRequest.optionStockUpdates.entries))
                            .flatMap { entry ->
                                productOptionRepository.findById(entry.key)
                                    .switchIfEmpty(Mono.error(Exception("option not found")))
                                        .flatMap { option ->
                                            option.increaseStock(entry.value)
                                            productOptionRepository.save(option)
                                        }
                            }
                            .then(Mono.just(ProductInfoResponse.fromProduct(product)))
                }
    }

    fun getAllProductsPaged(page: Int): Mono<Page<Product>> {
        val pageable = PageRequest.of(page, pageSize)
        val products = productRepository.findAllPageable(pageable).collectList()
        val totalCount = productRepository.count()

        return products.flatMap{ products ->
            totalCount.flatMap { totalCount ->
                Mono.just(PageImpl(products, pageable, totalCount))
            }
        }

    }

}