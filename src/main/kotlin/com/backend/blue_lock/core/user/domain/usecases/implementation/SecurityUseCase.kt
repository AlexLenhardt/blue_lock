package com.backend.blue_lock.core.user.domain.usecases.implementation

import com.backend.blue_lock.core.mail.MailerService
import com.backend.blue_lock.core.shared.utils.Utils
import com.backend.blue_lock.core.user.domain.entities.*
import com.backend.blue_lock.core.user.domain.errors.*
import com.backend.blue_lock.core.user.domain.repository.SecurityRepository
import com.backend.blue_lock.core.user.domain.usecases.SecurityUseCase
import com.backend.blue_lock.core.user.domain.usecases.response.GeneratePasswordResponse
import com.backend.blue_lock.core.user.security.SystemUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class SecurityUseCase(
    private val securityRepository: SecurityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailerService: MailerService,
) : SecurityUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(SecurityUseCase::class.java)
    }

    @Value("\${system.security.password.resetLimitMinutes}")
    private var passwordResetTimeLimit: Long = 0

    @Value("\${application.domainName}")
    private lateinit var domainName: String

    @Value("\${system.security.enableEncryption}")
    private var enableEncryption: Boolean = false

    override fun findUser(identifier: String): User? {
        try {
            securityRepository.findUserByUUID(UUID.fromString(identifier))?.run {
                return this
            }
        } catch (ignored: IllegalArgumentException) {
            // we can ignore if the provided identifier is not a UUID
        }
        securityRepository.findUserByEmail(identifier)?.run {
            return this
        }
        securityRepository.findUserByAuthenticationRecord(identifier)?.run {
            return this
        }
        return null
    }

    override fun userRoles(user: User): List<String> {
        return if (user.userType!!.code == EnumUserType.Owner.value)
            securityRepository.listOwnerRoles()
        else
            securityRepository.listUserRoles(user.uuid!!)
    }

    override fun createUser(user: User): User {
        val generated = randomPassword()
        val password = generated.first
        val passwordHash = generated.second
        val createdUser = user.copy(
            uuid = UUID.randomUUID(),
            password = password,
            passwordHash = passwordHash,
        )

        securityRepository.createUser(createdUser)

        if (!createdUser.email.isNullOrEmpty()) {
            // todo: create a better html template for this message
            mailerService.simple(
                createdUser.email,
                "",
                "WELCOME ABOARD",
                """
                    Welcome aboard, your credentials are:
                    Login: ${user.authenticationRecord}
                    Password: $password
                """.trimIndent()
            )

            return createdUser.copy(
                passwordHash = null,
                password = null,
            )
        }

        return createdUser.copy(passwordHash = null)
    }

    override fun generateNewPassword(authenticationRecord: String): GeneratePasswordResponse {
        val user = findUser(authenticationRecord)
            ?: return GeneratePasswordResponse(error = USER_NOT_FOUND)

        val generated = randomPassword()
        val password = generated.first
        val passwordHash = generated.second

        try {
            securityRepository.updateUserPassword(user.uuid!!, passwordHash, true)
        } catch (e: Exception) {
            return GeneratePasswordResponse(error = USER_STORAGE_ERROR)
        }

        return GeneratePasswordResponse(password)
    }

    override fun updateUserPassword(request: PasswordChangeRequest): PasswordChangeResponse {
        val systemUser = SecurityContextHolder.getContext().authentication.principal as SystemUser
        return if (request.newPassword == request.newPasswordConfirmation) {
            securityRepository.updateUserPassword(systemUser.uuid, passwordEncoder.encode(request.newPassword))
            PasswordChangeResponse(true)
        } else {
            PasswordChangeResponse(false, SECURITY_PASSWORD_CONFIRMATION_DOES_NOT_MATCH)
        }
    }

    override fun requestPasswordReset(email: String): StartPasswordResetResponse {
        val user = securityRepository.findUserByEmail(email)
            ?: return StartPasswordResetResponse(false, null, INVALID_CREDENTIALS)

        val requestUUID = UUID.randomUUID()
        val expiresAt = LocalDateTime.now().plusMinutes(passwordResetTimeLimit)

        val scheme = if (enableEncryption) "https://" else "http://"
        val param = "requestID=${Base64.getEncoder().encodeToString(requestUUID.toString().toByteArray())}"
        val resetPasswordLink = "${scheme}${domainName}/web/ChangePassword?${param}"

        return try {
            securityRepository.createPasswordResetRequest(user.uuid!!, requestUUID, expiresAt)

            // todo: create a better html template for this message
            mailerService.simple(
                email,
                "",
                "RESET PASSWORD REQUEST",
                """
                    Link to reset your password: $resetPasswordLink
                """.trimIndent()
            )

            StartPasswordResetResponse(true, expiresAt)
        } catch (e: Exception) {
            StartPasswordResetResponse(false, null, SECURITY_STORAGE_ERROR)
        }
    }

    override fun executePasswordReset(
        requestID: String,
        password: String,
        passwordConfirmation: String,
    ): ExecutePasswordResetResponse {
        return try {
            val requestUUID = UUID.fromString(String(Base64.getDecoder().decode(requestID)))
            val userUUID = securityRepository.findUserFromPasswordResetRequest(requestUUID)
            if (userUUID != null) {
                if (password != passwordConfirmation) {
                    ExecutePasswordResetResponse(false, SECURITY_PASSWORD_CONFIRMATION_DOES_NOT_MATCH)
                } else {
                    securityRepository.updateUserPassword(userUUID, passwordEncoder.encode(password))
                    securityRepository.deletePasswordResetRequest(requestUUID)

                    logger.info("Reset the password for user UUID [$userUUID]")
                    ExecutePasswordResetResponse(true)
                }
            } else {
                ExecutePasswordResetResponse(false, SECURITY_PASSWORD_RESET_REQUEST_EXPIRED)
            }
        } catch (e: Exception) {
            println(e.message)
            ExecutePasswordResetResponse(false, SECURITY_STORAGE_ERROR)
        }
    }

    private fun randomPassword(): Pair<String, String> {
        val generatedPassword = Utils.getRandomString(8)
        val generatedPasswordHash = passwordEncoder.encode(generatedPassword)

        return Pair(generatedPassword, generatedPasswordHash)
    }
}