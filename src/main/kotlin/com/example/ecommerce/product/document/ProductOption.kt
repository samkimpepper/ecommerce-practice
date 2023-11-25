package com.example.ecommerce.product.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ProductOption(
    @Id
    var id: String? = null,

    val price: Int,

    val size: String,

    var stock: Int,

    val productId: String,
) {
    fun increaseStock(amount: Int) {
        stock += amount
    }

    fun decreaseStock(amount: Int) {
        stock -= amount
    }
}
