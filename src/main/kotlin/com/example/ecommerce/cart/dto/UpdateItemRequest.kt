package com.example.ecommerce.cart.dto

data class UpdateItemRequest(
    val itemId: String,
    val quantity: Int,
)
