package com.example.ecommerce.product

import com.example.ecommerce.product.dto.RestockRequest
import com.example.ecommerce.product.dto.SaveProductOptionRequest
import com.example.ecommerce.product.dto.SaveProductRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProductHandler(
        private val productService: ProductService,
) {

    fun saveProduct(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(SaveProductRequest::class.java)
                .flatMap { product ->
                    productService.saveProduct(product)
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
}