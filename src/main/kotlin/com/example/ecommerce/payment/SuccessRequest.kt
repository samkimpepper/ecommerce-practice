package com.example.ecommerce.payment

import java.time.Instant

data class SuccessRequest(
    val imp_uid: String,
    val merchant_uid: String,
    val buyer_name: String,
    val buyer_email: String,
    val buyer_tel: String,
    val paid_amount: Int,
    val paid_at: Long,
    val pay_method: String,
    val pg_provider: String,
    val receipt_url: String,
)