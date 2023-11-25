package com.example.ecommerce.product.dto

data class ProductPageResponse(
        val name: String,
        val price: Int,
        val image: String?,
        val description: String?,
        val stock: Int,
        val wishCount: Int,
)
