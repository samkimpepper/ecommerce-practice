package com.example.ecommerce.order

data class OrderFromCartRequest(
    val paymentMethod: String,
    val deliveryAddressId: String,
)