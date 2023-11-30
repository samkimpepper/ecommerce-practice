package com.example.ecommerce.payment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class PaymentRouter(
    private val paymentHandler: PaymentHandler,
) {

    @Bean
    fun paymentRoutes() = router {
        "/api/payment".nest {
            POST("/ready", paymentHandler::ready)
            PUT("/success", paymentHandler::success)
            GET("/{paymentId}", paymentHandler::getPaymentInfo)
        }
    }
}