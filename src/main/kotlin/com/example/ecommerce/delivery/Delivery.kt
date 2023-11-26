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

        @Field("order_item_id", targetType = FieldType.OBJECT_ID)
        val orderItemIds: List<String>,

        @Field("customer_id", targetType = FieldType.OBJECT_ID)
        val customerId: String,

        @Field("shipper_id", targetType = FieldType.OBJECT_ID)
        val shipperId: String,

        @Field("delivery_address_id", targetType = FieldType.OBJECT_ID)
        val deliveryAddressId: String,

        var status: DeliveryStatus = DeliveryStatus.PREPARING,

        var trackingNumber: String,

        var estimatedDeliveryDate: Instant,

        val startedAt: Instant = Instant.now(),

        val deliveredAt: Instant? = null,
)
