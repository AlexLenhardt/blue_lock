package com.backend.blue_lock.core.user.security

import com.backend.blue_lock.core.shared.utils.Utils
import com.backend.blue_lock.core.user.domain.usecases.SecurityUseCase
import com.backend.blue_lock.core.user.domain.entities.*
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/auth")
@RestController
class LoginEndpoint(
    private val tokenHelper: TokenHelper,
    private val securityUseCase: SecurityUseCase,
    private val authenticationManager: AuthenticationManager,
    ) {
    @PostMapping("/login")
    fun authenticate(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<User> {
        val authentication = authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))

        SecurityContextHolder.getContext().authentication = authentication

        val user = (authentication.principal as SystemUser).getUserData()


        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, tokenHelper.generateJwtCookie(user.uuid!!).toString())
            .body(user)
    }

    @RequestMapping(
        value = ["/logout"],
        method = [RequestMethod.GET, RequestMethod.POST],
    )
    fun logoutUser(): ResponseEntity<*>? {
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, tokenHelper.getCleanJwtCookie().toString())
            .body("logged out")
    }

    @PostMapping("/requestPasswordReset")
    fun resetPassword(
        @RequestBody request: StartPasswordResetRequest,
    ): StartPasswordResetResponse = securityUseCase.requestPasswordReset(request.email)

    @PostMapping("/passwordReset/{requestID}")
    fun passwordReset(
        @PathVariable("requestID") requestID: String,
        @RequestBody request: PasswordResetChangeRequest,
    ): ExecutePasswordResetResponse =
        securityUseCase.executePasswordReset(requestID, request.password, request.passwordConfirmation)
}
