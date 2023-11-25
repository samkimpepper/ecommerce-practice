package com.example.ecommerce.user

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class UserRouter(
        private val userHandler: UserHandler,
) {
    @Bean
    fun userRoutes() = router {
        "/api/user".nest {
            POST("/register", userHandler::register)
            POST("/login", userHandler::login)
            GET("/", userHandler::currentUser)
        }
    }
}