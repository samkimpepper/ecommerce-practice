package com.example.ecommerce.delivery

data class InitiateRequest(
    val orderId: String,
    val shipperId: String,
)
