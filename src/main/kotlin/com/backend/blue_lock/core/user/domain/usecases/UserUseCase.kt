package com.backend.blue_lock.core.user.domain.usecases

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.usecases.response.UserResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserTypeResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserFilter
import com.backend.blue_lock.core.user.domain.usecases.response.UserListAllResponse

/**
 * The actions allowed to be performed with user
 */
interface UserUseCase {
    /**
     * Create or update a user in the system
     */
    fun postUser(user: User): UserResponse

    /**
     * Returns all user types in the system
     */
    fun listUserType(): UserTypeResponse

    /**
     * Get a user by the identifier
     */
    fun getUser(userID: String): UserResponse

    /**
     * List all users in the system
     */
    fun listAllUsers(
        page: Int,
        size: Int,
        orderBy: String,
        sortBy: String,
        filters: List<UserFilter>?,
    ): UserListAllResponse
}