package com.example.ecommerce.order.dto

data class SingleProductRequest(
    val productId: String,
    val quantityPerOptionId: Map<String, Int>,
    val deliveryAddressId: String,
    val paymentMethod: String,
)
