package com.backend.blue_lock.core.user.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.UUID
import com.backend.blue_lock.core.user.domain.entities.User as IUser

class SystemUser(
    private val user: IUser,
    private val roles: List<GrantedAuthority>,
) : User(
    user.authenticationRecord,
    user.passwordHash,
    true,
    true,
    true,
    true,
    roles,
) {

    /**
     * Returns the user data, removing the user password hash
     */
    fun getUserData(): IUser = user.copy(password = null)


    /**
     * Returns the user data, removing the user password hash
     */
    val uuid: UUID
        get() = user.uuid!!
}