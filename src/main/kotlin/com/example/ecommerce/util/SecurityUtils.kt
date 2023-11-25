package com.example.ecommerce.util

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono

object SecurityUtils {
    fun currentUser(): Mono<String> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication }
            .map { it.principal }
            .cast(String::class.java)
    }
}