package com.example.ecommerce.order.document

enum class OrderStatus {
    ORDER_RECEIVED,
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    PROCESSING,
    SHIPPING_PENDING,
    SHIPPED,
    DELIVERED
}