package com.backend.blue_lock.core.user.domain.entities

import com.backend.blue_lock.core.user.domain.errors.SecurityError

/**
 * Data required to reset a user password.
 *
 * This data must be used in together with either:
 *  - the current authentication scope, to reset the current user password
 */
data class PasswordChangeRequest(
    /**
     * The current password of this user.
     */
    val currentPassword: String,

    /**
     * The user authentication record
     */
    val newPassword: String,

    /**
     * The user authentication record
     */
    val newPasswordConfirmation: String
)

/**
 * Simple response for the password change
 */
data class PasswordChangeResponse(
    /**
     * Whether the request was successful
     */
    val success: Boolean = true,

    /**
     * A description of the error that stopped the generation of password
     */
    val error: SecurityError? = null,
)