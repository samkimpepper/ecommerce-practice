package com.example.ecommerce.product

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class ProductRouter(
        private val productHandler: ProductHandler,
) {

    @Bean
    fun productRoutes() = router {
        "/api/product".nest {
            POST("/", productHandler::saveProduct)
            GET("/{productId}", productHandler::getProductInfo)
        }
    }

    @Bean
    fun productOptionRoutes() = router {
        "/api/product/option".nest {
            POST("/", productHandler::saveProductOption)
            PUT("/restock", productHandler::restock)
        }
    }
}