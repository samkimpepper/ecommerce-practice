package com.example.ecommerce.cart

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CartItemRepository: ReactiveMongoRepository<CartItem, String> {
    fun findAllByCartId(cartId: String): Flux<CartItem>

    fun findByOptionId(optionId: String): Mono<CartItem>

}