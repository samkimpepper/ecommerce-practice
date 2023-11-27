package com.example.ecommerce.user

enum class Role {
    MERCHANT,
    SHIPPER;

    companion object {
        fun convert(role: String): Role {
            return values().firstOrNull { it.name.equals(role, ignoreCase = true)}
                ?: throw IllegalArgumentException("Invalid role")
        }
    }
}