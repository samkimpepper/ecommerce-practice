package com.example.ecommerce.cancellation

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CancellationHandler(
    private val cancellationService: CancellationService,
) {

    fun cancelBeforeShipment(request: ServerRequest): Mono<ServerResponse> {
        val orderId = request.pathVariable("orderId")
        return cancellationService.cancel(orderId)
            .flatMap { payment ->
                ServerResponse.ok().bodyValue(payment)
            }
    }
}