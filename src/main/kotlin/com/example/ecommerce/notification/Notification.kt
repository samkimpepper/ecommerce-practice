package com.example.ecommerce.notification

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

@Document
data class Notification(
    @Id
    var id: String? = null,

    val type: NotificationType,

    val title: String,

    val content: String,

    val link: String? = null,

    @Field("receiver_id", targetType = FieldType.OBJECT_ID)
    val receiverId: String,

    var readAt: Instant? = null,

    val createdAt: Instant = Instant.now(),

)
