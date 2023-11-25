package com.example.ecommerce.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

@Document
data class DeliveryAddress(
        @Id
        var id: String? = null,

        @Indexed
        @Field("user_id", targetType = FieldType.OBJECT_ID)
        val userId: String,

        val postCode: String,

        val isDefault: Boolean,

        var detailAddress: String,
)
