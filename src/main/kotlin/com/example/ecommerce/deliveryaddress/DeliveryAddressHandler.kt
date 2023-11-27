package com.example.ecommerce.deliveryaddress

import com.example.ecommerce.user.UserRepository
import com.example.ecommerce.util.SecurityUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class DeliveryAddressHandler(
    private val deliveryAddressService: DeliveryAddressService,
    private val userRepository: UserRepository,
) {

    fun save(request: ServerRequest): Mono<ServerResponse> {
        return SecurityUtils.currentUser()
            .flatMap { email ->
                userRepository.findByEmail(email)
            }
            .flatMap { user ->
                request.bodyToMono(SaveRequest::class.java)
                    .flatMap { saveRequest ->
                        deliveryAddressService.save(saveRequest, user)
                    }
                    .flatMap { deliveryAddress ->
                        ServerResponse.ok().bodyValue(deliveryAddress)
                    }

            }
    }
}