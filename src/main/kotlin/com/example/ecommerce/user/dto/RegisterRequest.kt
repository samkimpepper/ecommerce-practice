package com.example.ecommerce.user.dto

data class RegisterRequest(
        val email: String,
        val nickname: String,
        val password: String,
        val role: String?,
)
