package com.example.ecommerce.payment

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant

@Document
data class Payment(
    @Id
    var merchantUid: String? = null,

    var impUid: String? = null,

    var amount: Int? = null,

    var paymentMethod: String? = null,

    var receiptUrl: String? = null,

    var status: PaymentStatus = PaymentStatus.READY,

    var provider: PaymentProvider? = null,

    var buyerName: String? = null,

    var buyerEmail: String? = null,

    var buyerTel: String? = null,

    var paidAt: Instant? = null,

    var cancelledAt: Instant? = null,

    @Field("buyer_id", targetType = FieldType.OBJECT_ID)
    val buyerId: String,

    @Field("order_id", targetType = FieldType.OBJECT_ID)
    val orderId: String,

    ) {

    fun success(request: SuccessRequest) {
        impUid = request.imp_uid
        amount = request.paid_amount
        paymentMethod = request.pay_method
        receiptUrl = request.receipt_url
        status = PaymentStatus.PAID
        provider = PaymentProvider.convert(request.pg_provider)
        buyerName = request.buyer_name
        buyerEmail = request.buyer_email
        buyerTel = request.buyer_tel
        paidAt = Instant.ofEpochMilli(request.paid_at)
    }

    fun cancel() {
        status = PaymentStatus.CANCELLED
    }
}
