package com.example.ecommerce.delivery.dto

data class InitiateRequest(
    val orderItemIds: List<String>,
    val customerId: String,
    val shipperId: String,
    val deliveryAddressId: String,
)
