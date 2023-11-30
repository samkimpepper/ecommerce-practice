package com.example.ecommerce.order.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

@Document
data class Order(
        @Id
        var id: String? = null,

        @Field("customer_id", targetType = FieldType.OBJECT_ID)
        val customerId: String,

        @Field("delivery_address_id", targetType = FieldType.OBJECT_ID)
        val deliveryAddressId: String,

        val itemSummary: String,

        val totalAmount: Int,

        val totalShippingCost: Int,

        val paymentMethod: PaymentMethod,

        var status: OrderStatus = OrderStatus.ORDER_RECEIVED,

        val orderedAt: Instant = Instant.now(),
) {
        fun paymentSucceed() {
                status = OrderStatus.PAYMENT_COMPLETED
        }

        fun cancel() {
                status = OrderStatus.CANCELLED
        }
}
