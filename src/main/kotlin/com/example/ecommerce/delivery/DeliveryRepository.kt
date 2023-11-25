package com.example.ecommerce.delivery

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryRepository: ReactiveMongoRepository<Delivery, String> {
}