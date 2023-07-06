package com.backend.blue_lock.core.user.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import com.backend.blue_lock.core.user.domain.usecases.SecurityUseCase

@Service
class UserDetailsService(
    private val securityUseCase: SecurityUseCase
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        return securityUseCase.findUser(username)?.run {
            SystemUser(this, securityUseCase.userRoles(this).map { SimpleGrantedAuthority(it) })
        }
    }
}