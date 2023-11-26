package com.example.ecommerce.deliveryaddress

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface DeliveryAddressRepository: ReactiveMongoRepository<DeliveryAddress, String> {


}