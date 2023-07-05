package com.backend.blue_lock.core.user.domain.repository

import com.backend.blue_lock.core.user.domain.entities.User
import java.time.LocalDateTime
import java.util.*

/**
 * This interface defines duplicated methods overlapping with UserRepository, yet this implementation is intended to be
 * used for security reasons, and the methods in this contract include the internal security data, such as password
 * hashes and the like.
 */
interface SecurityRepository {
    /**
     * Creates a new user in the system
     */
    fun createUser(user: User)

    /**
     * Creates a new password reset request
     */
    fun createPasswordResetRequest(userUUID: UUID, requestUUID: UUID, expiresAt: LocalDateTime)

    /**
     * Returns the UUID of the user associated with the password reset request, if the request has not expired, returns
     * null otherwise.
     */
    fun findUserFromPasswordResetRequest(requestUUID: UUID): UUID?

    /**
     * Deletes the password reset request with the provided uuid.
     */
    fun deletePasswordResetRequest(requestUUID: UUID)

    /**
     * Change the password on database and set to user reset the password
     */
    fun updateUserPassword(uuid: UUID, newPasswordHash: String, forcePasswordChange: Boolean = false)

    /**
     * Returns all the data related to the user matching this uuid, including the password hash
     */
    fun findUserByUUID(uuid: UUID): User?

    /**
     * Returns all the data related to the user matching this email, including the password hash
     */
    fun findUserByEmail(email: String): User?

    /**
     * Returns all the data related to the user with the matching authentication record, including the password hash
     */
    fun findUserByAuthenticationRecord(authenticationRecord: String): User?

    /**
     * Return the list of security roles associated with the user with the provided UUID
     */
    fun listUserRoles(uuid: UUID): List<String>

    /**
     * Return the list of security roles associated with the user owner
     */
    fun listOwnerRoles(): List<String>
}