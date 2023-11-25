package com.example.ecommerce.user

import com.example.ecommerce.user.dto.LoginRequest
import com.example.ecommerce.user.dto.RegisterRequest
import org.springframework.http.MediaType
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(
        private val userService: UserService,
) {

    fun register(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(RegisterRequest::class.java)
                .flatMap { user ->
                    userService.register(user)
                }
                .flatMap { tokenResponse ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(tokenResponse)
                }
    }

    fun login(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(LoginRequest::class.java)
                .flatMap { user ->
                    userService.login(user)
                }
                .flatMap { tokenResponse ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(tokenResponse)
                }
    }

    fun currentUser(request: ServerRequest): Mono<ServerResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map { context -> context.authentication.principal }
                .cast(String::class.java)
                .flatMap { email ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(email)
                }
    }

}