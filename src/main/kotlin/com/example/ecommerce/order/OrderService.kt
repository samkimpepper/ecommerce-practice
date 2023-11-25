package com.example.ecommerce.order

import com.example.ecommerce.cart.Cart
import com.example.ecommerce.cart.CartItem
import com.example.ecommerce.order.document.Order
import com.example.ecommerce.order.document.OrderItem
import com.example.ecommerce.order.document.PaymentMethod
import com.example.ecommerce.user.DeliveryAddress
import com.example.ecommerce.user.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun placeOrderFromCart(orderFromCartRequest: OrderFromCartRequest, user: User, deliveryAddress: DeliveryAddress, cart: Cart, cartItems: List<CartItem>): Mono<Void> {
        val totalAmount = cart.totalPrice
        val totalShippingCost = cart.totalShippingCost
        val paymentMethod = PaymentMethod.convert(orderFromCartRequest.paymentMethod)

        var order = Order(
            userId = user.id!!,
            deliveryAddressId = deliveryAddress.id!!,
            totalAmount = totalAmount,
            totalShippingCost = totalShippingCost,
            paymentMethod = paymentMethod,
        )

        return orderRepository.save(order)
            .flatMap { savedOrder ->
                val orderItems = cartItems.map { cartItem ->
                    OrderItem(
                        orderId = savedOrder.id!!,
                        productId = cartItem.productId,
                        optionId = cartItem.optionId,
                        productName = cartItem.productName,
                        optionSize = cartItem.optionSize,
                        quantity = cartItem.quantity,
                        price = cartItem.price,
                    )
                }
                orderItemRepository.saveAll(orderItems)
                    .then(Mono.just(savedOrder to orderItems))
            }
            .doOnSuccess { (savedOrder, orderItems) ->
                eventPublisher.publishEvent(OrderCreatedEvent(savedOrder, orderItems))
            }
            .then()

    }
}