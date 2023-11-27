package com.example.ecommerce.user

import com.example.ecommerce.user.dto.LoginRequest
import com.example.ecommerce.user.dto.RegisterRequest
import com.example.ecommerce.user.dto.TokenResponse
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@Service
class UserService(
        private val userRepository: UserRepository,
        private val authService: AuthService,
) {

    fun register(registerRequest: RegisterRequest): Mono<TokenResponse> {

        return userRepository.existsByEmail(registerRequest.email)
                .handle { exists, sink ->
                    if(exists) {
                        println("duplicated error")
                        sink.error(RuntimeException("Duplicated email"))
                    }
                    val encodedPassword = authService.encodePassword(registerRequest.password)
                    val roles = mutableListOf("ROLE_USER")
                    if (registerRequest.role == "merchant")
                        roles.add("ROLE_MERCHANT")
                    else if (registerRequest.role == "shipper")
                        roles.add("ROLE_SHIPPER")

                    val user = User(
                            email = registerRequest.email,
                            password = encodedPassword,
                            nickname = registerRequest.nickname,
                            roles = roles,
                    )
                    sink.next(user)

                }
                .flatMap {user ->
                    userRepository.save(user)
                }
                .flatMap { savedUser ->
                    authService.generateToken(savedUser)
                }.flatMap { token ->
                    Mono.just(TokenResponse(token))
                }
    }

    fun login(loginRequest: LoginRequest): Mono<TokenResponse> {
        return userRepository.findByEmail(loginRequest.email)
                .switchIfEmpty(Mono.error<User>(Exception("User not found")))
                .flatMap { user ->
                    if(!authService.isMatchedPassword(loginRequest.password, user.password!!)) {
                        Mono.error<TokenResponse>(Exception("Invalid password"))
                    }
                    authService.generateToken(user)
                }
                .map { token -> TokenResponse(token) }
                .onErrorResume { e ->
                    Mono.just(TokenResponse("${e.message}"))
                }
    }

}