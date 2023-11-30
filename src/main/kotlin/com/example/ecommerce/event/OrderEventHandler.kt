package com.example.ecommerce.event

import com.example.ecommerce.cart.CartService
import com.example.ecommerce.delivery.DeliveryService
import com.example.ecommerce.notification.NotificationRepository
import com.example.ecommerce.order.OrderCancelledEvent
import com.example.ecommerce.order.OrderCreatedEvent
import com.example.ecommerce.order.OrderItemRepository
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
    private val orderItemRepository: OrderItemRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val notificationRepository: NotificationRepository,
) {

    @EventListener
    fun onOrderCreated(event: OrderCreatedEvent) {
        val emptyCartMono = cartService.emptyCart(event.order.customerId)
        val decreaseStockFlux = Flux.fromIterable(event.orderItems)
            .flatMap { orderItem ->
                productOptionRepository.findById(orderItem.optionId!!)
                    .flatMap { option ->
                        option.decreaseStock(orderItem.quantity)
                        productOptionRepository.save(option)
                    }
            }
        val saveNotificationFlux = notificationRepository.saveAll(event.toNotifications())

        Flux.merge(emptyCartMono, decreaseStockFlux, saveNotificationFlux)
            .doOnError { e -> println("onOrderCreated error: ${e.message}") }
            .subscribe()
    }

    @EventListener
    fun onOrderCancelled(event: OrderCancelledEvent) {
        val increaseStockFlux = orderItemRepository.findAllByOrderId(event.order.id!!)
            .flatMap { orderItem ->
                productOptionRepository.findById(orderItem.optionId!!)
                    .flatMap { option ->
                        option.increaseStock(orderItem.quantity)
                        productOptionRepository.save(option)
                    }
            }

        val saveNotificationsFlux = orderItemRepository.findAllByOrderId(event.order.id!!)
            .collectList()
            .flatMapMany { orderItems ->
                notificationRepository.saveAll(event.toNotifications(orderItems))
            }

        Flux.merge(increaseStockFlux, saveNotificationsFlux)
            .doOnError { e -> println("onOrderCancelled error: ${e.message}") }
            .subscribe()
    }
//    @EventListener
//    fun onOrderCreated(event: OrderCreatedEvent) {
//        println(event.orderItems.get(0))
//
//        Mono.just(event)
//            .flatMap { cartService.emptyCart(event.order.customerId) }
//            .then(Flux.fromIterable(event.orderItems)
//                .flatMap { orderItem ->
//                    println("order item")
//                    productOptionRepository.findById(orderItem.optionId!!)
//                        .flatMap { option ->
//                            println("option")
//                            option.decreaseStock(orderItem.quantity)
//                            productOptionRepository.save(option)
//                        }
//                }.then()
//            )
//            .flatMap {
//                println("notification!!")
//                notificationRepository.saveAll(event.toNotifications())
//                    .then()
//            }
//            .doOnError { e ->
//                println("error: ${e.message}")
//            }
//            .subscribe()
//    }
}