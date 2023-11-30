package com.example.ecommerce.order

import com.example.ecommerce.order.document.Order
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface OrderRepository: ReactiveMongoRepository<Order, String> {

    @Query(value = "{ 'customer_id' : ?0 }", sort = "{ 'orderedAt': -1 }")
    fun findAllByCustomerIdSorted(customerId: String): Flux<Order>
}