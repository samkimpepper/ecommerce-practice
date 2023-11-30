package com.example.ecommerce.cancellation

import com.example.ecommerce.order.OrderCancelledEvent
import com.example.ecommerce.order.OrderRepository
import com.example.ecommerce.order.document.OrderStatus
import com.example.ecommerce.payment.IamportPaymentClient
import com.example.ecommerce.payment.Payment
import com.example.ecommerce.payment.PaymentRepository
import com.example.ecommerce.payment.PaymentStatus
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CancellationService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val iamportPaymentClient: IamportPaymentClient,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun cancel(orderId: String): Mono<Payment> {
        return orderRepository.findById(orderId)
            .flatMap { order ->
                if (order.status != OrderStatus.ORDER_RECEIVED) {
                    Mono.error<Void>(Exception("Order cannot be cancelled"))
                }
                order.cancel()
                orderRepository.save(order)
            }
            .flatMapMany {
                paymentRepository.findAllByOrderId(orderId)
            }
            .filter { payment ->
                payment.status == PaymentStatus.PAID
            }
            .next()
            .flatMap { payment ->
                if (payment.status != PaymentStatus.PAID)
                    Mono.error<Payment>(Exception("Invalid access"))
                orderRepository.findById(payment.orderId)
                    .flatMap { order ->
                        if (!OrderStatus.isCancellable(order.status))
                            Mono.error<Payment>(Exception("Order cannot be cancelled"))
                        iamportPaymentClient.cancelPayment(payment.impUid!!, payment.amount!!)
                            .flatMap { isCancelled ->
                                if (!isCancelled)
                                    Mono.error<Payment>(Exception("환불 실패"))
                                payment.cancel()
                                paymentRepository.save(payment)
                            }
                    }
            }
    }
}