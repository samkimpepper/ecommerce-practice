package com.example.ecommerce.order

import com.example.ecommerce.cart.CartItemRepository
import com.example.ecommerce.cart.CartRepository
import com.example.ecommerce.deliveryaddress.DeliveryAddressRepository
import com.example.ecommerce.order.dto.OrderFromCartRequest
import com.example.ecommerce.order.dto.SingleProductRequest
import com.example.ecommerce.product.ProductOptionRepository
import com.example.ecommerce.product.ProductRepository
import com.example.ecommerce.product.document.ProductOption
import com.example.ecommerce.user.UserRepository
import com.example.ecommerce.util.SecurityUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class OrderHandler(
    private val orderService: OrderService,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val deliveryAddressRepository: DeliveryAddressRepository,
    private val productRepository: ProductRepository,
    private val productOptionRepository: ProductOptionRepository,
) {

    fun placeOrderFromCart(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(OrderFromCartRequest::class.java)
            .flatMap { orderFromCartRequest ->
                SecurityUtils.currentUser()
                    .flatMap { email ->
                        userRepository.findByEmail(email)
                    }
                    .flatMap { user ->
                        cartRepository.findByUserId(user.id!!)
                            .flatMap { cart ->
                                cartItemRepository.findAllByCartId(cart.id!!)
                                    .collectList()
                                    .flatMap { cartItems ->
                                        deliveryAddressRepository.findById(orderFromCartRequest.deliveryAddressId)
                                            .flatMap { deliveryAddress ->
                                                orderService.placeOrderFromCart(orderFromCartRequest, user, deliveryAddress, cart, cartItems)
                                            }
                                    }
                                    .flatMap {
                                        ServerResponse.ok().build()
                                    }
                            }
                    }
            }
    }

    fun orderSingleProduct(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(SingleProductRequest::class.java)
            .flatMap { singleProductRequest ->
                SecurityUtils.currentUser()
                    .flatMap { email ->
                        userRepository.findByEmail(email)
                    }
                    .flatMap { user ->
                        productRepository.findById(singleProductRequest.productId)
                            .flatMap { product ->
                                createQuantityPerOptionMap(singleProductRequest.quantityPerOptionId)
                                    .flatMap { quantityPerOption ->
                                        orderService.orderSingleProduct(singleProductRequest, user, quantityPerOption, product)
                                    }
                            }
                    }
                    .flatMap {
                        ServerResponse.ok().build()
                    }
            }
    }

    fun getOrderInfo(serverRequest: ServerRequest): Mono<ServerResponse> {
        val orderId = serverRequest.pathVariable("orderId")
        return orderService.getOrderInfo(orderId)
            .flatMap { orderWithItem ->
                ServerResponse.ok().bodyValue(orderWithItem)
            }
    }

    fun cancelOrder(serverRequest: ServerRequest): Mono<ServerResponse> {
        val orderId = serverRequest.pathVariable("orderId")
        return orderService.cancelOrder(orderId)
            .flatMap {
                ServerResponse.ok().build()
            }
    }

    private fun createQuantityPerOptionMap(quantityPerOptionId: Map<String, Int>): Mono<Map<ProductOption, Int>> {
        return Flux.fromIterable(quantityPerOptionId.entries)
            .flatMap { entry ->
                productOptionRepository.findById(entry.key)
                    .map { option -> option to entry.value }
            }
            .collectMap({it.first}, {it.second})
    }
}