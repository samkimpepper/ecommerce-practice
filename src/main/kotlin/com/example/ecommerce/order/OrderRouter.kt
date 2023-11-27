package com.example.ecommerce.order

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class OrderRouter(
    private val orderHandler: OrderHandler,
) {

    @Bean
    fun orderRoutes() = router {
        "/api/order".nest {
            POST("/cart", orderHandler::placeOrderFromCart)
            POST("/single", orderHandler::orderSingleProduct)
        }
    }
}