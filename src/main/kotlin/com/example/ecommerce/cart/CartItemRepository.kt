package com.example.ecommerce.cart

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CartItemRepository: ReactiveMongoRepository<CartItem, String> {
    fun findAllByCartId(cartId: String): Flux<CartItem>

}