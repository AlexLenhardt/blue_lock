package com.backend.blue_lock.core.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.UnsupportedEncodingException

@Component
class LoggingFilter : OncePerRequestFilter() {
    companion object {
        private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = ContentCachingRequestWrapper(request)

        val responseWrapper = ContentCachingResponseWrapper(response)

        val startTime = System.currentTimeMillis()

        filterChain.doFilter(requestWrapper, responseWrapper)

        val timeTaken = System.currentTimeMillis() - startTime

        val requestBody = getStringValue(
            requestWrapper.contentAsByteArray,
            request.characterEncoding
        )


        val responseBody = if (response.contentType != "application/json") {
            ""
        } else {
            getStringValue(
                responseWrapper.contentAsByteArray,
                response.characterEncoding
            )
        }


        if (!request.requestURI.contains("/auth") &&
            !request.requestURI.contains("/user") &&
            !request.requestURI.contains("/favicon.ico")
        ) {
            logger.info("\n" +
                    "METHOD=${request.method};\n" +
                    "REQUEST=${request.requestURI};\n" +
                    "PARAMS=${request.parameterMap.map { "${it.key}:${it.value.map { value -> value }}" } };\n" +
                    "REQUEST PAYLOAD = ${
                        requestBody.replace(
                            "/(\r\n|\n|\r)/gm".toRegex(),
                            ""
                        )
                    };\n" +
                    "RESPONSE CODE =${response.status};\n" +
                    "RESPONSE = $responseBody;\n" +
                    "TIM TAKEN = $timeTaken".trimMargin()
            )
        }

        responseWrapper.copyBodyToResponse()
    }

    private fun getStringValue(contentAsByteArray: ByteArray, characterEncoding: String): String {
        try {
            return String(contentAsByteArray, 0, contentAsByteArray.size, charset(characterEncoding))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }
}