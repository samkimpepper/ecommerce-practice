package com.example.ecommerce.order

import com.example.ecommerce.order.dto.OrderAggregation
import com.example.ecommerce.order.dto.OrderWithItem
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class OrderAggregationRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val objectMapper: ObjectMapper,
) {

    fun findByIdWithInfo(orderId: String): Mono<OrderWithItem> {
        val match: MatchOperation = Aggregation.match(Criteria.where("_id").`is`(ObjectId(orderId)))

        val lookupOrderItems: LookupOperation = LookupOperation.newLookup()
            .from("orderItem")
            .localField("_id")
            .foreignField("order_id")
            .`as`("orderItems")
        val lookupDeliveryAddress: LookupOperation = LookupOperation.newLookup()
            .from("deliveryAddress")
            .localField("delivery_address_id")
            .foreignField("_id")
            .`as`("deliveryAddress")

        val aggregation: Aggregation = Aggregation.newAggregation(match, lookupOrderItems, lookupDeliveryAddress)

        return mongoTemplate.aggregate(aggregation, "order", Document::class.java)
            .next()
            .map { document ->
                val convertedDocument = objectMapper.convertValue(document, OrderAggregation::class.java)
                OrderWithItem.fromDocument(convertedDocument)
            }
    }
}