package com.example.ecommerce.order.dto

import com.example.ecommerce.order.document.OrderItem
import com.example.ecommerce.order.document.OrderStatus
import com.example.ecommerce.order.document.PaymentMethod
import java.time.Instant

data class OrderWithItem(
    val id: String,
    val deliveryAddress: String,
    val deliveryAddressId: String,
    val totalAmount: Int,
    val totalShippingCost: Int,
    val paymentMethod: String,
    val status: String,
    val orderedAt: String,
    val paidAt: String? = null,
    val orderItems: List<OrderItemAggregation>,
) {

    companion object {
        fun fromDocument(order: OrderAggregation): OrderWithItem {
            return OrderWithItem(
                id = order._id.toString(),
                deliveryAddress = order.deliveryAddress.get(0).detailAddress,
                deliveryAddressId = order.delivery_address_id.toString(),
                totalAmount = order.totalAmount,
                totalShippingCost = order.totalShippingCost,
                paymentMethod = order.paymentMethod.name,
                status = order.status.name,
                orderedAt = order.orderedAt.toString(),
                paidAt = order.paidAt.toString(),
                orderItems = order.orderItems,
            )
        }
    }
}
