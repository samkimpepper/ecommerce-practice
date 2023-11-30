package com.example.ecommerce.event

import com.example.ecommerce.notification.Notification
import com.example.ecommerce.notification.NotificationRepository
import com.example.ecommerce.order.OrderItemRepository
import com.example.ecommerce.order.OrderRepository
import com.example.ecommerce.payment.PaymentSucceedEvent
import com.example.ecommerce.user.UserRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class PaymentEventHandler(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) {

    @EventListener
    fun onPaymentSucceed(event: PaymentSucceedEvent) {
        val orderMono = orderRepository.findById(event.orderId)
            .flatMap { order ->
                order.paymentSucceed()
                orderRepository.save(order)
            }
            .flatMap { order ->
                orderItemRepository.findAllByOrderId(order.id!!)
                    .collectList()
                    .map { orderItems ->
                        val notifications = event.toNotifications(orderItems)
                        notificationRepository.saveAll(notifications)
                    }
            }

        val buyerMono = userRepository.findById(event.buyerId)
            .flatMap { buyer ->
                val notification = event.toNotification()
                notificationRepository.save(notification)
            }

        Flux.merge(orderMono, buyerMono)
            .doOnError { e -> println("onPaymentSucceed error: ${e.message}") }
            .subscribe()

    }
}