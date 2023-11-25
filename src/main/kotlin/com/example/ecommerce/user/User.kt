package com.example.ecommerce.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id
    var id: String? = null,

    @Indexed(unique = true)
    val email: String,

    var nickname: String?,

    var password: String? = null,

    var isAdmin: Boolean = false,

    var lastLoginTimeStamp: Long = System.currentTimeMillis(),

    var provider: String? = null,

    var roles: List<String>,
)
