package com.example.ecommerce.order

import com.example.ecommerce.cart.Cart
import com.example.ecommerce.cart.CartItem
import com.example.ecommerce.order.document.Order
import com.example.ecommerce.order.document.OrderItem
import com.example.ecommerce.order.document.PaymentMethod
import com.example.ecommerce.deliveryaddress.DeliveryAddress
import com.example.ecommerce.order.document.OrderStatus
import com.example.ecommerce.order.dto.OrderFromCartRequest
import com.example.ecommerce.order.dto.OrderWithItem
import com.example.ecommerce.order.dto.SingleProductRequest
import com.example.ecommerce.product.document.Product
import com.example.ecommerce.product.document.ProductOption
import com.example.ecommerce.user.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderAggregationRepository: OrderAggregationRepository,
    private val orderItemRepository: OrderItemRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun placeOrderFromCart(orderFromCartRequest: OrderFromCartRequest, user: User, deliveryAddress: DeliveryAddress, cart: Cart, cartItems: List<CartItem>): Mono<Void> {
        val totalAmount = cart.totalPrice
        val totalShippingCost = cart.totalShippingCost
        val paymentMethod = PaymentMethod.convert(orderFromCartRequest.paymentMethod)

        var order = Order(
            customerId = user.id!!,
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
                        merchantId = cartItem.merchantId,
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

    fun orderSingleProduct(singleProductRequest: SingleProductRequest, user: User, quantityPerOption: Map<ProductOption, Int>, product: Product): Mono<Void> {
        val totalAmount = calculateTotalAmount(quantityPerOption)
        val totalShippingCost = calculateShippingCost(totalAmount)
        val paymentMethod = PaymentMethod.convert(singleProductRequest.paymentMethod)

        val order = Order(
            customerId = user.id!!,
            deliveryAddressId = singleProductRequest.deliveryAddressId,
            totalAmount = totalAmount,
            totalShippingCost = totalShippingCost,
            paymentMethod = paymentMethod,
        )

        return orderRepository.save(order)
            .flatMap { savedOrder ->
                val orderItems = quantityPerOption.entries.map { (option, quantity) ->
                    OrderItem(
                        orderId = savedOrder.id!!,
                        productId = option.productId!!,
                        optionId = option.id!!,
                        merchantId = product.merchantId,
                        productName = product.name,
                        optionSize = option.size,
                        quantity = quantity,
                        price = option.price * quantity,
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

    fun getOrderInfo(orderId: String): Mono<OrderWithItem> {
        return orderAggregationRepository.findByIdWithInfo(orderId)
    }

    private fun calculateTotalAmount(quantityPerOption: Map<ProductOption, Int>): Int {
        val totalAmount = quantityPerOption.entries.sumOf { (option, quantity) ->
            option.price * quantity
        }
        return totalAmount
    }

    private fun calculateShippingCost(totalAmount: Int): Int {
        return if (totalAmount >= 30_000) 0 else 2500
    }
}