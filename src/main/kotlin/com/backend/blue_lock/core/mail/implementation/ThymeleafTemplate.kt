package com.backend.blue_lock.core.mail.implementation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class ThymeleafTemplate: WebMvcConfigurer {
    @Bean
    @Description("Thymeleaf template resolver serving HTML 5")
    fun templateResolver(): ClassLoaderTemplateResolver? {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"
        templateResolver.isCacheable = false
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = "UTF-8"
        return templateResolver
    }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver())
        return templateEngine
    }

}