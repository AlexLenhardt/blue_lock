package com.backend.blue_lock.core.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler
import java.util.*


/**
 * This is the controller responsible for serving the frontend application.
 * It was set up to user the /web path, and the rest of the pathing/routing of the application, is handled by React
 */
@Controller
class WebPanel {
    /**
     * Use thymeleaf integration to simplify returning the index page, without any further code/setting.
     * The thymeleaf integration returns renders and return the template with the returned name, in the
     * "/main/resources/templates" directory.
     */
    @RequestMapping("/")
    fun ok(): String = "index"

    /**
     * Use thymeleaf integration to simplify returning the index page, without any further code/setting.
     * The thymeleaf integration returns renders and return the template with the returned name, in the
     * "/main/resources/templates" directory.
     */
    @RequestMapping("/web/**")
    fun index(): String = "index"
}

/**
 * This configuration handles the favicon for the application server
 */
@Configuration
class FaviconConfiguration {
    @Bean
    fun customFaviconHandlerMapping() = SimpleUrlHandlerMapping().apply {
        order = Int.MIN_VALUE
        urlMap = Collections.singletonMap("/static/favicons/favicon.ico", faviconRequestHandler())
    }

    @Bean
    protected fun faviconRequestHandler() = ResourceHttpRequestHandler().apply {
        locations = listOf<Resource>(ClassPathResource("/"))
    }
}