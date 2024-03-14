package com.backend.blue_lock.core.user.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(value = ["passwordHash"])
data class User(
    /**
     * The Universally Unique Identifier of this type
     */
    val uuid: UUID?,

    /**
     * The name of the user
     */
    val name: String?,

    /**
     * The type of the user(Owner, admin, default)
     */
    val userType: UserType?,

    /**
     * If is true, user is active else user is inactive
     */
    val isActive: Boolean,

    /**
     * The e-mail of the user
     */
    val email: String?,

    /**
     * The credential of login to access the system
     */
    val authenticationRecord: String?,

    /**
     * The password used to access the system
     */
    val password: String? = null,

    /**
     * The password hash used to the authentication and authorization of the users in the system
     */
    val passwordHash: String? = null,

    /**
     * The contact of the user, can be cellphone, fax, personal email...
     */
    val contact: String?,

    /**
     * Whether the user password needs to be changed
     */
    val isPasswordExpired: Boolean = false,
)

/**
 * Represents a user type in the system
 */
data class UserType(
    /**
     * The Universally Unique Identifier of this type
     */
    val uuid: UUID,

    /**
     * The external identifier to user type
     */
    val code: Int?,

    /**
     * The label of the type
     */
    val label: String?,
)

enum class EnumUserType(val value: Int) {
    Owner(1),
    Admin(2),
    Employee(3),
}