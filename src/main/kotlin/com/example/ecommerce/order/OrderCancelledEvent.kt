package com.example.ecommerce.order

import com.example.ecommerce.notification.Notification
import com.example.ecommerce.notification.NotificationType
import com.example.ecommerce.order.document.Order
import com.example.ecommerce.order.document.OrderItem
import org.springframework.context.ApplicationEvent
import java.io.File.separator

class OrderCancelledEvent(val order: Order): ApplicationEvent(order) {

    fun toNotifications(orderItems: List<OrderItem>): List<Notification> {
        val merchantToItemsGroups = orderItems.groupBy { it.merchantId }

        return merchantToItemsGroups.map { (merchantId, items) ->
            val orderDetails = items.joinToString(separator = "\n") {
                "주문번호 ${it.id!!}: 상품명: ${it.productName}, 사이즈: ${it.optionSize}, 수량: ${it.quantity}"
            }

            val content = StringBuilder()
            content.append("$orderDetails")

            Notification(
                type = NotificationType.ORDER_CANCELLED,
                title = "주문 취소",
                content = content.toString(),
                link = "",
                receiverId = merchantId!!,
            )
        }
    }
}
