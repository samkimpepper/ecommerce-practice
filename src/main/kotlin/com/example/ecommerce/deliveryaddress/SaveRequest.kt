package com.example.ecommerce.deliveryaddress

data class SaveRequest(
    val postCode: String,
    val isDefault: Boolean,
    val detailAddress: String,
)
