package ru.otus.spring.kushchenko.todolist.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * Created by Elena on Nov, 2018
 */
object Util {
    private val objectMapper = Jackson2ObjectMapperBuilder().build<ObjectMapper>()

    fun Any?.asJsonString(): String {
        try {
            return objectMapper.writeValueAsString(this)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }
}