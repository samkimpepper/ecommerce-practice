package com.example.ecommerce.product.dto

//data class RestockRequest(
//        val productId: String,
//        val productOptionId: String,
//        val amount: Int,
//)

data class RestockRequest (
    val productId: String,
    val optionStockUpdates: Map<String, Int>
    )
