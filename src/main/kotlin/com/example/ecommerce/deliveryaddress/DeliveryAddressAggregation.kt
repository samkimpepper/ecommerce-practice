package com.example.ecommerce.deliveryaddress

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

data class DeliveryAddressAggregation(
    val _id: ObjectId,
    val user_id: ObjectId,
    val postCode: String,
    val isDefault: Boolean,
    val detailAddress: String,
)
