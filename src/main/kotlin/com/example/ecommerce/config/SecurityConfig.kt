package com.example.ecommerce.config

import com.example.ecommerce.auth.JwtAuthenticationConverter
import com.example.ecommerce.auth.JwtAuthenticationManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(
            http: ServerHttpSecurity,
            authManager: JwtAuthenticationManager,
            converter: JwtAuthenticationConverter,
    ): SecurityWebFilterChain {
        val filter = AuthenticationWebFilter(authManager)
        filter.setServerAuthenticationConverter(converter)

        return http
                .csrf { it.disable() }
                .cors { it.disable() }
                .formLogin { it.disable() }
                .authorizeExchange { exchanges ->
                    exchanges.pathMatchers("/api/user/register").permitAll()
                            .pathMatchers("/api/user/login").permitAll()
                            .anyExchange().authenticated()
                }
                .addFilterAfter(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build()
    }
}