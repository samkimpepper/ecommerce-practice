package com.example.ecommerce.delivery

data class InitiateRequest(
    val orderItemIds: List<String>,
    val customerId: String,
    val shipperId: String,
    val deliveryAddressId: String,
)
