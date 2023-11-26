package com.example.ecommerce.deliveryaddress

import com.example.ecommerce.user.User
import org.springframework.stereotype.Service

@Service
class DeliveryAddressService(
    private val deliveryAddressRepository: DeliveryAddressRepository,
) {

    fun save(saveRequest: SaveRequest, user: User) {
        val deliveryAddress = DeliveryAddress(
            userId = user.id!!,
            postCode = saveRequest.postCode,
            isDefault = saveRequest.isDefault,
            detailAddress = saveRequest.detailAddress,
        )

        deliveryAddressRepository.save(deliveryAddress).subscribe()
    }
}