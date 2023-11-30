package com.example.ecommerce.payment

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository: ReactiveMongoRepository<Payment, String> {
}