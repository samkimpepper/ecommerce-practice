package com.example.ecommerce.payment

import com.example.ecommerce.payment.dto.ReadyResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val iamportPaymentClient: IamportPaymentClient,
) {
    fun ready(orderId: String, buyerId: String): Mono<ReadyResponse> {
        val payment = Payment(
            orderId = orderId,
            buyerId = buyerId,
        )

        return paymentRepository.save(payment)
            .flatMap { savedPayment ->
                iamportPaymentClient.getToken()
                    .flatMap { token ->
                        Mono.just(ReadyResponse(savedPayment.merchantUid!!, token))
                    }
            }
    }

    fun success(request: SuccessRequest): Mono<Payment> {
        return paymentRepository.findById(request.merchant_uid)
            .flatMap { payment ->
                iamportPaymentClient.validatePaymentAmount(request.imp_uid, request.paid_amount)
                    .flatMap { isValid ->
                        if(!isValid)
                            Mono.error<Payment>(Exception("결제 금액 불일치"))
                        payment.success(request)
                        paymentRepository.save(payment)
                    }
            }
    }
}