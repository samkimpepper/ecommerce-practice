package com.example.ecommerce.payment

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class IamportPaymentClient(
    @Value("\${iamport.api-key}")
    private val apiKey: String,

    @Value("\${iamport.api-secret}")
    private val apiSecret: String,
) {
    private val REQUEST_TOKEN_URL = "https://api.iamport.kr/users/getToken"
    private val REQUEST_VALIDATE_URL = "https://api.iamport.kr/payments/"
    private val REQUEST_CANCEL_URL = "https://api.iamport.kr/payments/cancel"

    fun getToken(): Mono<String> {
        val webClient = WebClient.create(REQUEST_TOKEN_URL)

        val body = TokenRequestBody(apiKey, apiSecret)

        return webClient.post()
            .uri(REQUEST_TOKEN_URL)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(TokenResponseBody::class.java)
            .map { response ->
                response.response.access_token
            }
    }

    fun validatePaymentAmount(impUid: String, amount: Int): Mono<Boolean> {
        return getToken().flatMap { token ->
            val webClient = WebClient.create()

            webClient.post()
                .uri("$REQUEST_VALIDATE_URL$impUid")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(PaymentResponseBody::class.java)
                .map { response ->
                    amount == response.response.amount
                }
        }
    }

    fun cancelPayment(impUid: String, amount: Int): Mono<Boolean> {
        return getToken().flatMap { token ->
            val webClient = WebClient.create()

            val body = CancelRequestBody(
                imp_uid = impUid,
                amount = amount,
                checksum = amount,
            )

            webClient.post()
                .uri(REQUEST_CANCEL_URL)
                .bodyValue(body)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(PaymentResponseBody::class.java)
                .map { response ->
                    response.code == 200
                }
        }
    }
}

data class TokenRequestBody(
    val imp_key: String,
    val imp_secret: String,
)

data class TokenResponseBody(
    val code: Int,
    val message: String?,
    val response: InnerResponse,
) {
    data class InnerResponse(
        val access_token: String,
        val now: Long,
        val expired_at: Long,
    )
}

data class PaymentResponseBody(
    val code: Int,
    val message: String?,
    val response: InnerResponse,
) {
    data class InnerResponse(
        val imp_uid: String,
        val merchant_uid: String,
        val amount: Int,
        val cancel_amount: Int,
        val currency: String,
        val status: String,
    )
}

data class CancelRequestBody(
    val imp_uid: String,
    val amount: Int,
    val checksum: Int,
)