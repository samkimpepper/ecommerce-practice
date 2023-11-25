package com.example.ecommerce.cart

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import java.time.Instant

data class Cart(
        @Id
        var id: String? = null,

        @Indexed
        val userId: String,

        var totalQuantity: Int = 0,

        var totalPrice: Int = 0, // 배송비 제외 총 가격

        var totalShippingCost: Int = 0,

        var updatedAt: Instant = Instant.now(),
)
