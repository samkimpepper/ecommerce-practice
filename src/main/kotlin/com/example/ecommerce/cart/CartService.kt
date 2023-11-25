package com.example.ecommerce.cart

import com.example.ecommerce.cart.dto.AddItemRequest
import com.example.ecommerce.cart.dto.CartWithItems
import com.example.ecommerce.cart.dto.UpdateItemRequest
import com.example.ecommerce.product.ProductRepository
import com.example.ecommerce.product.document.Product
import com.example.ecommerce.product.document.ProductOption
import com.example.ecommerce.user.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CartService(
        private val cartRepository: CartRepository,
        private val cartItemRepository: CartItemRepository,
        private val productRepository: ProductRepository,
) {
    private val freeShippingThreshold = 30_000

    fun getUserCart(user: User): Mono<CartWithItems> {
        return cartRepository.findByUserId(user.id!!)
                .switchIfEmpty(cartRepository.save(Cart(userId = user.id!!)))
                .flatMap { cart ->
                    cartItemRepository.findAllByCartId(cart.id!!)
                            .collectList()
                            .map { items -> CartWithItems.fromCart(cart, items) }
                }
    }

    private fun getCartWithItem(cart: Cart): Mono<CartWithItems> {
        return cartItemRepository.findAllByCartId(cart.id!!)
            .collectList()
            .map { items -> CartWithItems.fromCart(cart, items) }
    }

    private fun getCartWithItem(cart: Cart, items: List<CartItem>): Mono<CartWithItems> {
        return Mono.just(CartWithItems.fromCart(cart, items))
    }

    fun addItem(addItemRequest: AddItemRequest, user: User, product: Product, productOption: ProductOption): Mono<CartWithItems> {
        return cartRepository.findByUserId(user.id!!)
            .switchIfEmpty(cartRepository.save(Cart(userId = user.id!!)))
            .flatMap { cart ->
                val item = CartItem(
                        cartId = cart.id!!,
                        productId = product.id!!,
                        optionId = productOption.id!!,
                        productName = product.name,
                        optionSize = productOption.size,
                        quantity = addItemRequest.quantity,
                        price = productOption.price * addItemRequest.quantity,
                )
                cartItemRepository.save(item).thenReturn(cart)
            }
            .flatMap { cart ->
                updateCartInfo(cart)
                getCartWithItem(cart)
            }
    }

    fun updateItemQuantity(updateItemRequest: UpdateItemRequest, user: User): Mono<CartWithItems> {
        return cartItemRepository.findById(updateItemRequest.itemId)
            .flatMap { item ->
                item.quantity = updateItemRequest.quantity
                cartItemRepository.save(item)
            }
            .flatMap { item ->
                cartRepository.findById(item.cartId)
                    .flatMap { cart ->
                        updateCartInfo(cart)
                        getCartWithItem(cart)
                    }
            }
    }

    fun emptyCart(userId: String): Mono<Void> {
        return cartRepository.findByUserId(userId)
            .flatMap { cart ->
                cart.totalPrice = 0
                cart.totalShippingCost = 0
                cart.totalQuantity = 0
                val items = cartItemRepository.findAllByCartId(cart.id!!)
                cartItemRepository.deleteAll(items)
            }
    }

    fun emptyCart(user: User): Mono<Void> {
        return cartRepository.findByUserId(user.id!!)
            .flatMap { cart ->
                cart.totalPrice = 0
                cart.totalShippingCost = 0
                cart.totalQuantity = 0
                val items = cartItemRepository.findAllByCartId(cart.id!!)
                cartItemRepository.deleteAll(items)
            }
    }

    private fun updateCartInfo(cart: Cart): Mono<Cart> {
        return cartItemRepository.findAllByCartId(cart.id!!)
                .collectList()
                .flatMap { items ->
                    calculateShippingCost(items)
                        .flatMap { shippingCost ->
                            cart.totalQuantity = items.sumOf { it.quantity }
                            cart.totalPrice = items.sumOf { it.price }
                            cart.totalShippingCost = shippingCost

                            cartRepository.save(cart)
                        }
                }
    }

    private fun calculateShippingCost(items: List<CartItem>): Mono<Int> {
        var totalShippingCost = 0

        return productRepository.findAllById(items.map { it.productId })
            .collectList()
            .map { products ->
                val productToMerchantMap = products.associate { it.id to it.merchantId }
                val merchantToItemsMap: Map<String?, List<CartItem>> = items.groupBy { productToMerchantMap[it.productId] }

                merchantToItemsMap.forEach { (_, cartItems) ->
                    val firstItem = cartItems.firstOrNull()
                    firstItem?.let {
                        val product = products.find { p -> p.id == it.productId }
                        product?.let { p ->
                            totalShippingCost += p.shippingCost
                        }
                    }
                }
                totalShippingCost
            }
    }
}