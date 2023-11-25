package com.example.ecommerce.delivery

import com.example.ecommerce.order.document.Order
import org.springframework.stereotype.Service

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
) {

    fun initiate(order: Order) {

    }
}