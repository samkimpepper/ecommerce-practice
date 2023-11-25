package com.example.ecommerce.order.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

@Document
data class OrderItem(
    @Id
    var id: String? = null,

    @Field("order_id", targetType = FieldType.OBJECT_ID)
    val orderId: String,

    @Field("delivery_id", targetType = FieldType.OBJECT_ID)
    val deliveryId: String? = null,

    @Field("product_id", targetType = FieldType.OBJECT_ID)
    val productId: String?,

    @Field("option_id", targetType = FieldType.OBJECT_ID)
    val optionId: String?,

    var productName: String,

    var optionSize: String,

    val quantity: Int,

    val price: Int,
)
