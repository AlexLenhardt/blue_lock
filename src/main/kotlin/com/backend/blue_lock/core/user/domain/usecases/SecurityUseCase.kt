package com.backend.blue_lock.core.user.domain.usecases

import com.backend.blue_lock.core.user.domain.entities.*
import com.backend.blue_lock.core.user.domain.usecases.response.GeneratePasswordResponse
import org.springframework.stereotype.Component

/**
 * The security use case is the only place in the application allowed to manipulate user credentials and authentication
 * logic. Any usage of the User#passwordHash outside the security usecase and the respective security repository are
 * considered wrong, and should be moved to the appropriate "Security" classes.
 */
@Component
interface SecurityUseCase {
    /**
     * Create a new user and grants basic access to the system
     */
    fun createUser(user: User): User

    /**
     * Generate a new password to user that haven't email
     */
    fun generateNewPassword(authenticationRecord: String): GeneratePasswordResponse

    /**
     * Change the password on database and set to user reset the password
     */
    fun updateUserPassword(request: PasswordChangeRequest): PasswordChangeResponse

    /**
     * Starts the "reset password" sequence for the user with the provided authentication record and email.
     *
     * For users without an e-mail, passwords are generated in the user creation/modification request.
     * This method will return error in the following scenarios:
     *  - if the authentication record was not found
     *  - if the user associated with the authentication record does not have an e-mail
     *  - if the provided e-mail does not match with the e-mail associated to the user with the provided authenticationRecord
     */
    fun requestPasswordReset(email: String): StartPasswordResetResponse

    /**
     * Execute the password reset request with the received "requestID".
     */
    fun executePasswordReset(
        requestID: String,
        password: String,
        passwordConfirmation: String
    ): ExecutePasswordResetResponse

    /**
     * Find a user with the existing identifier
     */
    fun findUser(identifier: String): User?

    /**
     * Returns the list of roles associated with the user with the provided UUID
     */
    fun userRoles(user: User): List<String>
}