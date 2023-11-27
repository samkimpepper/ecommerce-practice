package com.example.ecommerce.delivery

import com.example.ecommerce.notification.Notification
import com.example.ecommerce.notification.NotificationType
import org.springframework.context.ApplicationEvent

class DeliveryInitiatedEvent(val delivery: Delivery): ApplicationEvent(delivery){
    fun toNotification(): Notification {
        return Notification(
            type = NotificationType.DELIVERY_INITIATED,
            title = "배송 등록",
            content = "",
            link = "/api/delivery/${delivery.id!!}",
            receiverId = delivery.shipperId,
        )
    }
}
