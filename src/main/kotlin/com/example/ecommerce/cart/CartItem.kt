package com.example.ecommerce.cart

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import java.time.Instant

data class CartItem(
        @Id
        var id: String? = null,

        @Indexed
        val cartId: String,

        val productId: String,

        var optionId: String,

        var productName: String,

        var optionSize: String,

        var quantity: Int,

        var price: Int,

        val createdAt: Instant = Instant.now(),
)
