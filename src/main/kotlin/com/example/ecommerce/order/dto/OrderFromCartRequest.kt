package com.example.ecommerce.order.dto

data class OrderFromCartRequest(
    val paymentMethod: String,
    val deliveryAddressId: String,
)