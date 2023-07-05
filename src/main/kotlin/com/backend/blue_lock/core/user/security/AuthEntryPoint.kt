package com.backend.blue_lock.core.user.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * Always throws authentication exception, since the user authentication is based in tokens added in each request
 * cookies. The token is generated in the custom login endpoint /login (handled by SystemLoginEndpoint class).
 *
 * Based on: https://www.bezkoder.com/spring-boot-login-example-mysql/
 */
@Component
class AuthEntryPoint : AuthenticationEntryPoint {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationEntryPoint::class.java)
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthorized: ${authException.message}")

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        ObjectMapper().writeValue(
            response.outputStream, mapOf(
                "status" to HttpServletResponse.SC_UNAUTHORIZED,
                "error" to "Unauthorized",
                "message" to authException.message,
                "path" to request.servletPath
            )
        )
    }
}