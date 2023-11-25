package com.example.ecommerce.order.document

enum class PaymentMethod {
    CREDIT_CARD,
    BANK_TRANSFER,
    POINTS;

    companion object {
        fun convert(method: String): PaymentMethod {
            return values().firstOrNull { it.name.equals(method, ignoreCase = true)}
                ?: throw IllegalArgumentException("Invalid payment method")
        }
    }
}
