package com.example.ecommerce.auth

import com.example.ecommerce.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(val user: User): UserDetails {
    var enabled: Boolean = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
             AuthorityUtils.createAuthorityList(*user.roles.toTypedArray())

    override fun getPassword() = user.password

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = enabled

    override fun isAccountNonLocked() = enabled

    override fun isCredentialsNonExpired() = enabled

    override fun isEnabled(): Boolean = enabled
}