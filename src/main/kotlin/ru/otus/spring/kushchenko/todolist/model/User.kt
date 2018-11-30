package ru.otus.spring.kushchenko.todolist.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Elena on Nov, 2018
 */
@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    @Indexed(unique = true)
    val username: String,
    @get:JsonIgnore
    val password: String,
    val email: String,
    val roles: List<Role>? = listOf(Role.USER),
    val enabled: Boolean? = true
)