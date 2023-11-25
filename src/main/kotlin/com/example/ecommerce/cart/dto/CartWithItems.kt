package com.example.ecommerce.cart.dto

import com.example.ecommerce.cart.Cart
import com.example.ecommerce.cart.CartItem

data class CartWithItems(
    val totalQuantity: Int,
    val totalPrice: Int,
    val totalShippingCost: Int,
    val items: List<ItemInfo>,
) {
    companion object {
        fun fromCart(cart: Cart, items: List<CartItem>): CartWithItems {
            return CartWithItems(
                    totalQuantity = cart.totalQuantity,
                    totalPrice = cart.totalPrice,
                    totalShippingCost = cart.totalShippingCost,
                    items = ItemInfo.fromItem(items),
            )
        }
    }
}

data class ItemInfo(
        val itemId: String,
        val productName: String,
        val optionSize: String,
        val quantity: Int,
        val price: Int,
) {
    companion object {
        fun fromItem(items: List<CartItem>): List<ItemInfo> {
            return items.map { item ->
                ItemInfo(
                        itemId = item.id!!,
                        productName = item.productName,
                        optionSize = item.optionSize,
                        quantity = item.quantity,
                        price = item.price,
                )
            }
        }
    }
}