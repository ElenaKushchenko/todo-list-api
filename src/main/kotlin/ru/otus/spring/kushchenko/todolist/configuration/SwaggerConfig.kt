package ru.otus.spring.kushchenko.todolist.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Created by Elena on Nov, 2018
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        val token = ParameterBuilder()
            .name("Authorization")
            .modelRef(ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build()

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ru.otus.spring.kushchenko.todolist.controller"))
            .paths(PathSelectors.any())
            .build()
            .globalOperationParameters(listOf(token))
    }
}