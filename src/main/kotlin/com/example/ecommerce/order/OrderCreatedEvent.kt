package com.example.ecommerce.order

import com.example.ecommerce.order.document.Order
import com.example.ecommerce.order.document.OrderItem
import org.springframework.context.ApplicationEvent

class OrderCreatedEvent(val order: Order, val orderItems: List<OrderItem>): ApplicationEvent(order) {


}