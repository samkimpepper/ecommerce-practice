package com.example.ecommerce.product

import com.example.ecommerce.product.document.ProductOption
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ProductOptionRepository: ReactiveMongoRepository<ProductOption, String> {

    fun findAllByProductId(productId: String): Flux<ProductOption>
}