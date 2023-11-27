package com.example.ecommerce.deliveryaddress

import com.example.ecommerce.user.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeliveryAddressService(
    private val deliveryAddressRepository: DeliveryAddressRepository,
) {

    fun save(saveRequest: SaveRequest, user: User): Mono<DeliveryAddress> {
        val deliveryAddress = DeliveryAddress(
            userId = user.id!!,
            postCode = saveRequest.postCode,
            isDefault = saveRequest.isDefault,
            detailAddress = saveRequest.detailAddress,
        )

        return deliveryAddressRepository.save(deliveryAddress)
    }
}