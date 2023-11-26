package com.example.ecommerce.order

import com.example.ecommerce.notification.Notification
import com.example.ecommerce.notification.NotificationType
import com.example.ecommerce.order.document.Order
import com.example.ecommerce.order.document.OrderItem
import org.springframework.context.ApplicationEvent
import java.lang.StringBuilder

class OrderCreatedEvent(val order: Order, val orderItems: List<OrderItem>): ApplicationEvent(order) {

//    fun toNotifications(): List<Notification> {
//        return orderItems.map { orderItem ->
//            Notification(
//                type = NotificationType.ORDER_PLACED,
//                title = "주문 발생",
//                content = "상품명: ${orderItem.productName}, 사이즈: ${orderItem.optionSize}, 수량: ${orderItem.quantity}개 주문 발생",
//                link = "/api/delivery",
//                receiverId = orderItem.merchantId!!,
//            )
//        }
//    }

    fun toNotifications(): List<Notification> {
        val merchantToItemsGroups = orderItems.groupBy { it.merchantId }

        return merchantToItemsGroups.map { (merchantId, items) ->
            val orderDetails = items.joinToString(separator = "\n") {
                "상품 아이디: ${it.id!!} 상품명: ${it.productName}, 사이즈: ${it.optionSize}, 수량: ${it.quantity}개 주문 발생"
            }

            val content = StringBuilder()
            content.append("고객 ID: ${order.customerId}\n")
            content.append("배송 주소 ID: ${order.deliveryAddressId}\n")
            content.append("주문 상세 정보:\n$orderDetails")

            Notification(
                type = NotificationType.ORDER_PLACED,
                title = "주문 발생",
                content = content.toString(),
                link = "/api/delivery",
                receiverId = merchantId!!,
            )
        }
    }
 }