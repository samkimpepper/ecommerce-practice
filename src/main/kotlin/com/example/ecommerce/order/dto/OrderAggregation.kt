package com.example.ecommerce.order.dto

import com.example.ecommerce.deliveryaddress.DeliveryAddress
import com.example.ecommerce.deliveryaddress.DeliveryAddressAggregation
import com.example.ecommerce.order.document.OrderItem
import com.example.ecommerce.order.document.OrderStatus
import com.example.ecommerce.order.document.PaymentMethod
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

data class OrderAggregation(
    val _id: ObjectId,
    val customer_id: ObjectId,
    val delivery_address_id: ObjectId,
    val totalAmount: Int,
    val totalShippingCost: Int,
    val paymentMethod: PaymentMethod,
    val status: OrderStatus,
    val orderedAt: Instant,
    val paidAt: Instant?,

    val deliveryAddress: List<DeliveryAddressAggregation>,
    val orderItems: List<OrderItemAggregation>
)

data class OrderItemAggregation (
    val _id: ObjectId,
    val order_id: ObjectId,
    val delivery_id: ObjectId? = null,
    val product_id: ObjectId,
    val option_id: ObjectId,
    val merchant_id: ObjectId,
    val productName: String,
    val optionSize: String,
    val quantity: Int,
    val price: Int,
)