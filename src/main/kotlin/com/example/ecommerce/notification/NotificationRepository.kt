package com.example.ecommerce.notification

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface NotificationRepository: ReactiveMongoRepository<Notification, String> {

    fun findAllByReceiverId(receiverId: String): Flux<Notification>
}