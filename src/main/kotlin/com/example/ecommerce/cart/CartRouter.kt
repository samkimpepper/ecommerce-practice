package com.example.ecommerce.cart

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class CartRouter(
    private val cartHandler: CartHandler,
) {

    @Bean
    fun cartRoutes() = router {
        "/api/cart".nest {
            GET("/", cartHandler::getUserCart)
            DELETE("/", cartHandler::emptyCart)
        }
    }

    @Bean
    fun cartItemRoutes() = router {
        "/api/cart/item".nest {
            POST("/", cartHandler::addCartItem)
            PUT("/", cartHandler::updateCartItem)
        }
    }
}