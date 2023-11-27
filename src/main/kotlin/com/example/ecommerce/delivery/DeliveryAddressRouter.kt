package com.example.ecommerce.delivery

import com.example.ecommerce.deliveryaddress.DeliveryAddressHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class DeliveryAddressRouter(
    private val deliveryAddressHandler: DeliveryAddressHandler,
) {

    @Bean
    fun deliveryAddressRoutes() = router {
        "/api/delivery-address".nest {
            POST("/", deliveryAddressHandler::save)
        }
    }
}