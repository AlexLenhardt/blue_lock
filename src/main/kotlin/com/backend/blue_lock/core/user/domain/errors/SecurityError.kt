package com.backend.blue_lock.core.user.domain.errors

import com.backend.blue_lock.core.shared.error.GenericError

/**
 * Defines the common security related errors
 */
class SecurityError(
    code: String,
    description: String,
) : GenericError("security-module", code, description)

/**
 * Error for when the user provides the wrong credentials
 */
val INVALID_CREDENTIALS = SecurityError("INVALID_CREDENTIALS", "Invalid credentials")

/**
 * Error for when something unexpected happens in the repositoer layer
 */
val SECURITY_STORAGE_ERROR = SecurityError("SECURITY_STORAGE_ERROR", "Error in the server repository layer")

/**
 * Error for when the password reset request has expired
 */
val SECURITY_PASSWORD_RESET_REQUEST_EXPIRED =
    SecurityError("SECURITY_PASSWORD_RESET_REQUEST_EXPIRED", "Request expired")

/**
 * Error for when the provided password and password confirmation don't match
 */
val SECURITY_PASSWORD_CONFIRMATION_DOES_NOT_MATCH =
    SecurityError("SECURITY_PASSWORD_CONFIRMATION_DOES_NOT_MATCH", "Password confirmation does not match")