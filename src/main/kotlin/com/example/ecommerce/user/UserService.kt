package com.example.ecommerce.user

import com.example.ecommerce.user.dto.LoginRequest
import com.example.ecommerce.user.dto.RegisterRequest
import com.example.ecommerce.user.dto.TokenResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@Service
class UserService(
        private val userRepository: UserRepository,
        private val authService: AuthService,
) {

    fun register(registerRequest: RegisterRequest): Mono<TokenResponse> {
        return userRepository.existsByEmail(registerRequest.email)
                .flatMap { exists ->
                    if(exists) {
                        Mono.error<TokenResponse>(RuntimeException("Duplicated email"))
                    }
                    val encodedPassword = authService.encodePassword(registerRequest.password)
                    val roles = mutableListOf("ROLE_USER")
                    if (registerRequest.role == "merchant")
                        roles.add("ROLE_MERCHANT")

                    val user = User(
                            email = registerRequest.email,
                            password = encodedPassword,
                            nickname = registerRequest.nickname,
                            roles = roles,
                    )
                    userRepository.save(user)
                }.flatMap { savedUser ->
                    authService.generateToken(savedUser)
                }.flatMap { token ->
                    Mono.just(TokenResponse(token))
                }
    }

    fun login(loginRequest: LoginRequest): Mono<TokenResponse> {
        return userRepository.findByEmail(loginRequest.email)
                .flatMap { user ->
                    if(!authService.isMatchedPassword(loginRequest.password, user.password!!))
                        Mono.error<TokenResponse>(Exception("Invalid password"))
                    authService.generateToken(user)
                }
                .map { token -> TokenResponse(token) }
    }

}