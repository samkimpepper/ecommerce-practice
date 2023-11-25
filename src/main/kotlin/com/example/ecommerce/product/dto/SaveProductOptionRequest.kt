package com.example.ecommerce.product.dto

data class SaveProductOptionRequest(
        val productId: String,
        val price: Int,
        val size: String,
        val stock: Int,
)
