package com.example.ecommerce.delivery.dto

import org.bson.Document


data class DeliveryInfoResponse(
    val orderItems: List<OrderItemInfo>,
    val customerNickname: String,
    val merchantId: String,
    val merchantNickname: String,
    val deliveryAddress: String,
    val status: String,
    val trackingNumber: String,
    val estimatedDeliveryDate: String,
    val startedAt: String,
    val deliveredAt: String? = null,
) {
    companion object {
        fun fromDocument(delivery: DeliveryAggregation): DeliveryInfoResponse {
            val orderItems = delivery.orderItems.map { orderItem ->
                OrderItemInfo(
                    productName = orderItem.productName,
                    quantity = orderItem.quantity,
                )
            }

            return DeliveryInfoResponse(
                orderItems = orderItems,
                customerNickname = delivery.customer.nickname!!,
                merchantId = delivery.merchantId,
                merchantNickname = delivery.merchantId,
                deliveryAddress = delivery.deliveryAddress.detailAddress,
                status = delivery.status.name,
                trackingNumber = delivery.trackingNumber,
                estimatedDeliveryDate = delivery.estimatedDeliveryDate.toString(),
                startedAt = delivery.startedAt.toString(),
                deliveredAt = delivery.deliveredAt?.toString(),
            )

        }
    }
}

data class OrderItemInfo(
    val productName: String,
    val quantity: Int,
)

