package com.example.ecommerce.payment

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PaymentRepository: ReactiveMongoRepository<Payment, String> {
    fun findAllByOrderId(orderId: String): Flux<Payment>
}