package com.example.ecommerce.event

import com.example.ecommerce.delivery.DeliveryInitiatedEvent
import com.example.ecommerce.notification.NotificationRepository
import com.example.ecommerce.user.UserRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeliveryEventHandler(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
) {

    @EventListener
    fun onDeliveryInitiated(event: DeliveryInitiatedEvent) {
        Mono.just(event)
            .flatMap { notificationRepository.save(event.toNotification())
                .then()
            }
            .subscribe()
    }
}