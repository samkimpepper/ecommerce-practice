package com.example.ecommerce.payment.dto

data class ReadyResponse(
    val merchantUid: String,
    val token: String,
)
