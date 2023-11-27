package com.example.ecommerce.delivery

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class DeliveryRouter(
    private val deliveryHandler: DeliveryHandler,
) {

    @Bean
    fun deliveryRoutes() = router {
        "/api/delivery".nest {
            POST("/initiate", deliveryHandler::initiate)
        }
    }
}