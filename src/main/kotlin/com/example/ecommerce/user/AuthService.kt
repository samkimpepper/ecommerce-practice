package com.example.ecommerce.user

import com.example.ecommerce.auth.JwtAuthenticationManager
import com.example.ecommerce.auth.JwtProvider
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
        private val passwordEncoder: PasswordEncoder,
        private val jwtProvider: JwtProvider,
        private val authManager: JwtAuthenticationManager,
) {

    fun isMatchedPassword(password: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(password, encodedPassword)
    }

    fun encodePassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun generateToken(user: User): Mono<String> {
        val authenticationToken = UsernamePasswordAuthenticationToken(user.email, user.password)
        val securityContext = SecurityContextImpl(authenticationToken)


        return Mono.just(securityContext)
            .flatMap { context ->
                ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context))
                Mono.just(authenticationToken)
            }
            .then(Mono.just(jwtProvider.generate(authenticationToken.name).value))
            .onErrorResume {
                Mono.error(Exception("토큰 생성 실패"))
            }


    }

}