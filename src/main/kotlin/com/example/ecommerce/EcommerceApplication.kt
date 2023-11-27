package com.example.ecommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager

@SpringBootApplication
class EcommerceApplication

fun main(args: Array<String>) {
	runApplication<EcommerceApplication>(*args)
}
