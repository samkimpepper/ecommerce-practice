package com.example.ecommerce.delivery

import com.example.ecommerce.order.document.Order
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
) {

    fun initiate(initiateRequest: InitiateRequest) {
        val delivery = Delivery(
            orderItemIds = initiateRequest.orderItemIds,
            customerId = initiateRequest.customerId,
            shipperId = initiateRequest.shipperId,
            deliveryAddressId = initiateRequest.deliveryAddressId,
            trackingNumber = "123",
            estimatedDeliveryDate = calculateEstimatedDeliveryDate(),
        )

        deliveryRepository.save(delivery).subscribe()
    }

    private fun calculateEstimatedDeliveryDate(): Instant {
        return Instant.now().plus(48, ChronoUnit.HOURS)
    }
}