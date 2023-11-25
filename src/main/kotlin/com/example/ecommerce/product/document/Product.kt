package com.example.ecommerce.product.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

@Document
data class Product(
    @Id
    var id: String? = null,

    @Field("merchant_id", targetType = FieldType.OBJECT_ID)
    val merchantId: String,

    var name: String,

    var minimumPrice: Int = Int.MAX_VALUE,

    var shippingCost: Int,

    var image: String?,

    var description: String?,

    var wishCount: Int = 0,

    var stock: Int = 0,

    val createdAt: Instant = Instant.now(),
) {
    fun increaseStock(amount: Int) {
        stock += amount
    }

    fun decreaseStock(amount: Int) {
        stock -= amount
    }
}
