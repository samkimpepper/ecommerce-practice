package com.example.ecommerce.payment

import com.example.ecommerce.notification.Notification
import com.example.ecommerce.notification.NotificationType
import com.example.ecommerce.order.document.OrderItem
import org.springframework.context.ApplicationEvent

class PaymentSucceedEvent(val orderId: String, val buyerId: String) : ApplicationEvent(orderId){

    fun toNotifications(orderItems: List<OrderItem>): List<Notification> {
        val merchantToItemsGroups = orderItems.groupBy { it.merchantId }

        return merchantToItemsGroups.map { (merchantId, items) ->
            val orderDetails = items.joinToString(separator = "\n") {
                "상품 아이디: ${it.id!!} 상품명: ${it.productName}, 사이즈: ${it.optionSize}, 수량: ${it.quantity}개 주문 발생"
            }

            val content = StringBuilder()
            content.append(orderDetails)

            Notification(
                type = NotificationType.PURCHASE_CONFIRMED,
                title = "결제 완료",
                content = content.toString(),
                link = "",
                receiverId = merchantId!!,
            )
        }
    }

    fun toNotification(): Notification {
        return Notification(
            type = NotificationType.PURCHASE_CONFIRMED,
            title = "결제 완료",
            content = "주문번호 $orderId 결제가 완료되었습니다.",
            link = "",
            receiverId = buyerId,
        )
    }
}
