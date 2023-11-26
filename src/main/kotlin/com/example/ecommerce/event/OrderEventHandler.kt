package com.example.ecommerce.event

import com.example.ecommerce.cart.CartService
import com.example.ecommerce.delivery.DeliveryService
import com.example.ecommerce.notification.NotificationRepository
import com.example.ecommerce.order.OrderCreatedEvent
import com.example.ecommerce.product.ProductOptionRepository
import com.example.ecommerce.product.ProductRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class OrderEventHandler(
    private val cartService: CartService,
    private val deliveryService: DeliveryService,
    private val productOptionRepository: ProductOptionRepository,
    private val notificationRepository: NotificationRepository,
) {
    @EventListener
    fun onOrderCreated(event: OrderCreatedEvent) {

        Mono.just(event)
            .flatMap { cartService.emptyCart(event.order.customerId).then() }
            .then(Flux.fromIterable(event.orderItems)
                .flatMap { orderItem ->
                    productOptionRepository.findById(orderItem.optionId!!)
                        .flatMap { option ->
                            option.decreaseStock(orderItem.quantity)
                            productOptionRepository.save(option)
                        }
                }.then()
            )
            .flatMap {
                notificationRepository.saveAll(event.toNotifications())
                    .then()
            }
            .subscribe()
    }
}