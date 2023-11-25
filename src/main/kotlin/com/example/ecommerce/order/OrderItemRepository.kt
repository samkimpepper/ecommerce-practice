package com.example.ecommerce.order

import com.example.ecommerce.order.document.OrderItem
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface OrderItemRepository: ReactiveMongoRepository<OrderItem, String> {
    fun findAllByOrderId(orderId: String): Flux<OrderItem>
}