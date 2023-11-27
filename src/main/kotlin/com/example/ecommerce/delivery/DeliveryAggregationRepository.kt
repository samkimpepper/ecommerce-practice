package com.example.ecommerce.delivery

import com.example.ecommerce.delivery.dto.DeliveryAggregation
import com.example.ecommerce.delivery.dto.DeliveryInfoResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeliveryAggregationRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val objectMapper: ObjectMapper,
) {

    fun findByIdWithInfo(deliveryId: String): Mono<DeliveryInfoResponse> {
        val match: MatchOperation = Aggregation.match(Criteria.where("id").`is`(deliveryId))

        val lookupOrderItems: LookupOperation = LookupOperation.newLookup()
            .from("order_item")
            .localField("orderItemIds")
            .foreignField("_id")
            .`as`("orderItems")
        val lookupCustomer: LookupOperation = LookupOperation.newLookup()
            .from("user")
            .localField("customerId")
            .foreignField("_id")
            .`as`("customer")
        val lookupDeliveryAddress: LookupOperation = LookupOperation.newLookup()
            .from("delivery_address")
            .localField("deliveryAddressId")
            .foreignField("_id")
            .`as`("deliveryAddress")

        val aggregation: Aggregation = Aggregation.newAggregation(match, lookupOrderItems, lookupCustomer, lookupDeliveryAddress)

        return mongoTemplate.aggregate(aggregation, "delivery", Document::class.java)
            .next()
            .map { document ->
                val convertedDocument = objectMapper.convertValue(document, DeliveryAggregation::class.java)
                DeliveryInfoResponse.fromDocument(convertedDocument)
            }
    }


}