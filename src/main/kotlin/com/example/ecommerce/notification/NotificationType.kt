package com.example.ecommerce.notification

enum class NotificationType {
    ORDER_PLACED,
    DELIVERY_INITIATED,
    DELIVERY_STARTED,
    DELIVERY_COMPLETED,
    PURCHASE_CONFIRMED,
    DELIVERY_FAILED, // 배송은 완료되었는데 사용자가 못 받았을 때
}