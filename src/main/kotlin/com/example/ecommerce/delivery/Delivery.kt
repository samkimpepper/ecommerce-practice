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

        @Field("user_id", targetType = FieldType.OBJECT_ID)
        val userId: String,

        val deliveryAddressId: String,

        var status: DeliveryStatus = DeliveryStatus.PREPARING,

        var trackingNumber: String,

        var estimatedDeliveryDate: Instant,

        val startedAt: Instant? = null,

        val deliveredAt: Instant? = null,
)
