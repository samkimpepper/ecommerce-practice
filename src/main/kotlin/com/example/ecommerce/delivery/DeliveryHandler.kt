package com.example.ecommerce.delivery

import com.example.ecommerce.cart.CartItemRepository
import com.example.ecommerce.cart.CartRepository
import com.example.ecommerce.order.OrderItemRepository
import com.example.ecommerce.user.UserRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class DeliveryHandler(
    private val deliveryService: DeliveryService,
    private val userRepository: UserRepository,
    private val orderItemRepository: OrderItemRepository,
) {

    fun initiate(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(InitiateRequest::class.java)
            .flatMap { initiateRequest ->
                userRepository.findById(initiateRequest.customerId)
                    .flatMap { user ->
                        orderItemRepository.findAllById(initiateRequest.orderItemIds)
                            .collectList()
                            .flatMap { orderItems ->
                                deliveryService.initiate(initiateRequest)
                                ServerResponse.ok().build()
                            }
                    }
            }
    }
}