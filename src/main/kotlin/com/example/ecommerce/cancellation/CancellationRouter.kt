package com.example.ecommerce.cancellation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class CancellationRouter(
    private val cancellationHandler: CancellationHandler,
) {

    @Bean
    fun cancellationRoutes() = router {
        "/api/cancel".nest {
            PUT("/{orderId}", cancellationHandler::cancelBeforeShipment)
        }
    }
}