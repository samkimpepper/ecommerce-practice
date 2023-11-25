package com.example.ecommerce.cart.dto

data class AddItemRequest(
        val productId: String,
        val optionId: String,
        val quantity: Int,
)
