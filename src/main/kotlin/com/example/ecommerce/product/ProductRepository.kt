package com.example.ecommerce.product

import com.example.ecommerce.product.document.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


@Repository
interface ProductRepository: ReactiveMongoRepository<Product, String> {

    @Query("{}")
    fun findAllPageable(pageable: Pageable): Flux<Product>

    @Query("{'stock': {'\$gt': 0}}")
    fun findInStockProductsPageable(pageable: Pageable): Flux<Product>
}