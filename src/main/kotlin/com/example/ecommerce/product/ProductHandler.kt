package com.example.ecommerce.product

import com.example.ecommerce.product.dto.RestockRequest
import com.example.ecommerce.product.dto.SaveProductOptionRequest
import com.example.ecommerce.product.dto.SaveProductRequest
import com.example.ecommerce.user.UserRepository
import com.example.ecommerce.util.SecurityUtils
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProductHandler(
        private val productService: ProductService,
        private val userRepository: UserRepository,
) {

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    fun saveProduct(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(SaveProductRequest::class.java)
                .flatMap { product ->
                    SecurityUtils.currentUser()
                        .flatMap { email ->
                            userRepository.findByEmail(email)
                                .flatMap { user ->
                                    productService.saveProduct(product, user.id!!)
                                }
                        }
                }
                .flatMap { product ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(product)
                }
    }

    fun saveProductOption(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(SaveProductOptionRequest::class.java)
                .flatMap { option ->
                    productService.saveProductOption(option)
                }
                .flatMap { option ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(option)
                }
    }

    fun getProductInfo(request: ServerRequest): Mono<ServerResponse> {
        val productId = request.pathVariable("productId")

        return productService.getProductInfo(productId)
                .flatMap { productInfo ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(productInfo)
                }
    }

    fun restock(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(RestockRequest::class.java)
                .flatMap { request ->
                    productService.restock(request)
                }
                .flatMap { productInfo ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(productInfo)
                }
    }

    fun getAllProductsPaged(request: ServerRequest): Mono<ServerResponse> {
        val page = request.pathVariable("page").toInt()

        return productService.getAllProductsPaged(page)
            .flatMap { products ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(products)
            }
    }
}