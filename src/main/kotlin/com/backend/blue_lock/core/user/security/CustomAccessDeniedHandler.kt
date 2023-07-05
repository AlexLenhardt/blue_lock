package com.backend.blue_lock.core.user.security

import com.backend.blue_lock.core.user.domain.errors.ACCESS_DENIED
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpStatus.FORBIDDEN.value()
        // Defina o corpo da resposta de erro personalizada
        val errorResponse = ACCESS_DENIED
        val objectMapper = ObjectMapper()
        val errorJson = objectMapper.writeValueAsString(errorResponse)
        response.writer.write(errorJson)
    }
}
