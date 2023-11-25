package com.example.ecommerce.cart

import com.example.ecommerce.cart.dto.AddItemRequest
import com.example.ecommerce.cart.dto.UpdateItemRequest
import com.example.ecommerce.product.ProductOptionRepository
import com.example.ecommerce.product.ProductRepository
import com.example.ecommerce.user.UserRepository
import com.example.ecommerce.util.SecurityUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CartHandler(
    private val cartService: CartService,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val productOptionRepository: ProductOptionRepository,
) {

    fun getUserCart(request: ServerRequest): Mono<ServerResponse> {
        return SecurityUtils.currentUser()
            .flatMap { email ->
                userRepository.findByEmail(email)
            }
            .flatMap { user ->
                cartService.getUserCart(user)
            }
            .flatMap { cartWithItems ->
                ServerResponse.ok().bodyValue(cartWithItems)
            }
    }

    fun addCartItem(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(AddItemRequest::class.java)
            .flatMap { addItemRequest ->
                Mono.zip(
                    SecurityUtils.currentUser()
                        .flatMap { email ->
                            userRepository.findByEmail(email)
                        },
                    productRepository.findById(addItemRequest.productId),
                    productOptionRepository.findById(addItemRequest.optionId),
                )
                    .flatMap { tuple ->
                        val user = tuple.t1
                        val product = tuple.t2
                        val productOption = tuple.t3
                        cartService.addItem(addItemRequest, user, product, productOption)
                    }
            }
            .flatMap { cartWithItems ->
                ServerResponse.ok().bodyValue(cartWithItems)
            }
    }

    fun updateCartItem(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(UpdateItemRequest::class.java)
            .flatMap { updateItemRequest ->
                SecurityUtils.currentUser()
                    .flatMap { email ->
                        userRepository.findByEmail(email)
                    }
                    .flatMap { user ->
                        cartService.updateItemQuantity(updateItemRequest, user)
                    }
                    .flatMap { cartWithItems ->
                        ServerResponse.ok().bodyValue(cartWithItems)
                    }
            }
    }

    fun emptyCart(request: ServerRequest): Mono<ServerResponse> {
        return SecurityUtils.currentUser()
            .flatMap { email ->
                userRepository.findByEmail(email)
            }
            .flatMap { user ->
                cartService.emptyCart(user)
            }
            .then(ServerResponse.ok().build())
    }
}