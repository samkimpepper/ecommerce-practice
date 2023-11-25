package com.example.ecommerce.user

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository: ReactiveMongoRepository<User, String> {
    fun existsByEmail(email: String): Mono<Boolean>

    fun findByEmail(email: String): Mono<User>

}