package com.example.ecommerce.order.document

enum class OrderStatus {
    ORDER_RECEIVED,
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    PROCESSING,
    SHIPPING_PENDING,
    SHIPPED,
    DELIVERED,

    CANCELLED;

    companion object {
        fun isCancellable(status: OrderStatus): Boolean {
            return status == ORDER_RECEIVED ||
                    status == PAYMENT_PENDING ||
                    status == PAYMENT_COMPLETED ||
                    status == PROCESSING ||
                    status == SHIPPING_PENDING
        }
    }
}