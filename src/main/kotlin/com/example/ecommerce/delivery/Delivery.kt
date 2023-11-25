package com.example.ecommerce.delivery

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

@Document
data class Delivery(
        @Id
        var id: String? = null,

        @Field("order_id", targetType = FieldType.OBJECT_ID)
        val orderId: String,

        @Field("order_item_id", targetType = FieldType.OBJECT_ID)
        val orderItemId: String,

        @Field("customer_id", targetType = FieldType.OBJECT_ID)
        val customerId: String,

        val shipperId: String,

        val deliveryAddressId: String,

        var status: DeliveryStatus = DeliveryStatus.PREPARING,

        var trackingNumber: String,

        var estimatedDeliveryDate: Instant,

        val startedAt: Instant? = null,

        val deliveredAt: Instant? = null,
)
