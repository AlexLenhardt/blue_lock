package com.backend.blue_lock.core.user.infraestructure.webservice

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.domain.entities.PasswordChangeRequest
import com.backend.blue_lock.core.user.domain.entities.PasswordChangeResponse
import com.backend.blue_lock.core.user.domain.usecases.response.GeneratePasswordResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserListAllResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserTypeResponse
import org.springframework.web.bind.annotation.RequestParam

/**
 * Defines the actions available for user in the system
 */
interface UserService {
    /**
     * Get the user by some identifier
     *
     * @param userID can be any identifier of user(uuid, authenticationRecord or email).
     * Any of them are unique
     *
     * @return Will return all informations about the user
     */
    fun getUser(userID: String): UserResponse

    /**
     * Generate a new random password to user that doesn't have email
     */
    fun generateNewPassword(authenticationRecord: String): GeneratePasswordResponse

    /**
     * Updates the current user password
     */
    fun updatePassword(request: PasswordChangeRequest): PasswordChangeResponse

    /**
     * Create or update a user
     */
    fun postUser(user: User): UserResponse

    /**
     * Return all user types in the system
     */
    fun listUserType(): UserTypeResponse

    fun listAllUsers(
        @RequestParam("page", required = true, defaultValue = "1") page: Int,
        @RequestParam("size", required = true, defaultValue = "30") size: Int,
        @RequestParam("orderBy", required = false, defaultValue = "") orderBy: String,
        @RequestParam("sortBy", required = false, defaultValue = "desc") sortBy: String,
        @RequestParam("filter", required = false, defaultValue = "") filter: List<BasicFilter>?,
    ): UserListAllResponse
}