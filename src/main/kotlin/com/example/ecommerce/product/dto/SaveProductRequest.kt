package com.example.ecommerce.product.dto

data class SaveProductRequest(
        val name: String,
        val image: String?,
        val description: String?,
        val shippingCost: Int,
)
