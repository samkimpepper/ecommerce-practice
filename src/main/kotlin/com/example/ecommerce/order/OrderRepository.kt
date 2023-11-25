package com.example.ecommerce.order

import com.example.ecommerce.order.document.Order
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: ReactiveMongoRepository<Order, String> {
}