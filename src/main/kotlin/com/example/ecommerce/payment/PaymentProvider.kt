package com.example.ecommerce.payment

enum class PaymentProvider {
    TOSSPAY;

    companion object {
        fun convert(method: String): PaymentProvider {
            return PaymentProvider.values().firstOrNull() { it.name.equals(method, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid payment status")
        }
    }
}