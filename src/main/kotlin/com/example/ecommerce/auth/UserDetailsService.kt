package com.example.ecommerce.auth

import com.example.ecommerce.user.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserDetailsService(
        private val userRepository: UserRepository,
): ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return if (username == null) return Mono.empty()
        else {
            userRepository.findByEmail(username)
                    .map { user -> UserDetailsImpl(user) as UserDetails }
                    .switchIfEmpty(Mono.empty())
        }
    }
}