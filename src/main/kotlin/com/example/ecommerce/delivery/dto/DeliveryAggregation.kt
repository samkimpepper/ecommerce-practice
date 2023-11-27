package com.example.ecommerce.delivery.dto

import com.example.ecommerce.delivery.DeliveryStatus
import com.example.ecommerce.deliveryaddress.DeliveryAddress
import com.example.ecommerce.order.document.OrderItem
import com.example.ecommerce.user.User
import java.time.Instant

data class DeliveryAggregation(
    var id: String? = null,
    val orderItemIds: List<String>,
    val customerId: String,
    val merchantId: String,
    val shipperId: String,
    val deliveryAddressId: String,
    var status: DeliveryStatus = DeliveryStatus.PREPARING,
    var trackingNumber: String,
    var estimatedDeliveryDate: Instant,
    val startedAt: Instant = Instant.now(),
    val deliveredAt: Instant? = null,

    val orderItems: List<OrderItem>,
    val customer: User,
    val deliveryAddress: DeliveryAddress,
)
