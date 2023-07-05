package com.backend.blue_lock.core.user.domain.usecases.response

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.entities.UserType
import com.backend.blue_lock.core.user.domain.errors.UserException
import com.backend.blue_lock.core.shared.error.GenericError

data class GeneratePasswordResponse(
    /**
     * The password generated
     */
    val password: String? = null,

    /**
     * A description of the error that stopped the generation of password
     */
    val error: UserException? = null,
) {
    override fun toString(): String {
        return "GeneratePasswordResponse=($password, $error)"
    }
}

data class UserResponse(
    /**
     * The user that was inserted or updated on database
     */
    val user: User? = null,

    /**
     * A description of the error that stopped user types a from being created or updated
     */
    val error: UserException? = null,
) {
    override fun toString(): String {
        return "UserResponse=($user, $error)"
    }
}

/**
 * Entity returned whe listing user types
 */
data class UserTypeResponse(
    /**
     * The list of user types in the system
     */
    val userTypes: List<UserType>? = null,

    /**
     * A description of the error that stopped user types a from being listed
     */
    val error: UserException? = null,
)

/**
 * Response responsible to list all users
 */
data class UserListAllResponse(
    val users: List<User>? = null,
    val page: Int? = null,
    val size: Int? = null,
    val numberPages: Int? = null,
    var error: GenericError? = null
) {
    override fun toString(): String {
        return "userListAllResponse=($users, $page, $size , $numberPages, $error)"
    }
}