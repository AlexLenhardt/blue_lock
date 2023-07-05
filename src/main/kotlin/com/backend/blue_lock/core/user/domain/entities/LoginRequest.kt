package com.backend.blue_lock.core.user.domain.entities

/**
 * Basic data required to authenticate in the application
 */
data class LoginRequest(
    /**
     * The identifier of this user, can be one of the following:
     *  - UUID
     *  - E-mail
     *  - Authentication record
     */
    val username: String,

    /**
     * The password of this user
     */
    val password: String
)