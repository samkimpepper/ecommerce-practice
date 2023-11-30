package com.example.ecommerce.payment

import com.example.ecommerce.user.UserRepository
import com.example.ecommerce.util.SecurityUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class PaymentHandler(
    private val paymentService: PaymentService,
    private val userRepository: UserRepository,
) {

    fun ready(request: ServerRequest): Mono<ServerResponse> {
        val orderId = request.pathVariable("orderId")

        return SecurityUtils.currentUser()
            .flatMap { email ->
                userRepository.findByEmail(email)
            }
            .flatMap { user ->
                paymentService.ready(orderId, user.id!!)
            }
            .flatMap { response ->
                ServerResponse.ok().bodyValue(response)
            }
    }

    fun success(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(SuccessRequest::class.java)
            .flatMap { successRequest ->
                paymentService.success(successRequest)
            }
            .flatMap { payment ->
                ServerResponse.ok().bodyValue(payment)
            }

    }
}