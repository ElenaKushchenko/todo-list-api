package ru.otus.spring.kushchenko.todolist.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler

/**
 * Created by Elena on Nov, 2018
 */
@Configuration
class SecurityConfig {
    @Bean
    fun defaultMethodSecurityExpressionHandler() =
        DefaultMethodSecurityExpressionHandler()
            .apply { setDefaultRolePrefix("") }

    @Bean
    fun defaultWebSecurityExpressionHandler() =
        DefaultWebSecurityExpressionHandler()
            .apply { setDefaultRolePrefix("") }
}