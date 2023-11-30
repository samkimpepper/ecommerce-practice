package com.example.ecommerce.payment

enum class PaymentStatus {
    PAID,
    FAILED,
    READY;

    companion object {
        fun convert(method: String): PaymentStatus {
            return values().firstOrNull() { it.name.equals(method, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid payment status")
        }
    }
}