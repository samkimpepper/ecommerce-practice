package com.example.ecommerce.delivery

import com.example.ecommerce.delivery.dto.DeliveryInfoResponse
import com.example.ecommerce.delivery.dto.InitiateRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val deliveryAggregationRepository: DeliveryAggregationRepository,
) {

    fun initiate(initiateRequest: InitiateRequest, merchantId: String): Mono<Void> {
        val delivery = Delivery(
            orderItemIds = initiateRequest.orderItemIds,
            customerId = initiateRequest.customerId,
            merchantId = merchantId,
            shipperId = initiateRequest.shipperId,
            deliveryAddressId = initiateRequest.deliveryAddressId,
            trackingNumber = "123",
            estimatedDeliveryDate = calculateEstimatedDeliveryDate(),
        )

        return deliveryRepository.save(delivery).then()
    }

    fun getInfo(deliveryId: String): Mono<DeliveryInfoResponse> {
        return deliveryAggregationRepository.findByIdWithInfo(deliveryId)
    }

    private fun calculateEstimatedDeliveryDate(): Instant {
        return Instant.now().plus(48, ChronoUnit.HOURS)
    }
}