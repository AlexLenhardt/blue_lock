package com.backend.blue_lock.core.user.domain.repository

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.entities.UserType
import com.backend.blue_lock.core.user.domain.usecases.response.UserFilter
import java.util.*

interface UserRepository {
    /**
     * Get a user by him authentication record
     */
    fun getUserByAuthenticationRecord(authenticationRecord: String): User?

    /**
     * Update an existing user
     */
    fun updateUser(user: User): User

    /**
     * Get a specific user by UUID, used to verify if a user exist
     */
    fun getUserByUUID(uuid: UUID): User?

    /**
     * Get a specific user by email, used to verify if the e-mail is already used
     */
    fun getUserByEmail(email: String): User?

    /**
     * Get a specific user type, used to verify if a user type exist
     */
    fun getUserType(userType: UUID): UserType?

    /**
     * List all user types in the system
     */
    fun listUserType(): List<UserType>

    /**
     * Get the number of users
     */
    fun getCountUsers(filter: List<UserFilter>?): Int

    /**
     * List all users in the system
     */
    fun listAllUsers(
        firstItem: Int,
        size: Int,
        orderBy: String,
        sortBy: String,
        filter: List<UserFilter>?
    ): List<User>
}