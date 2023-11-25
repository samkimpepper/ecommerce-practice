package com.example.ecommerce.cart

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CartRepository: ReactiveMongoRepository<Cart, String> {

    fun findByUserId(userId: String): Mono<Cart>
}