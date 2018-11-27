package ru.otus.spring.kushchenko.todolist.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Elena on Nov, 2018
 */
@Document(collection = "projects")
data class Project(
    @Id
    val id: String? = null,
    @Indexed(unique = true)
    val name: String,
    val order: Int,
    val tasks: List<Task>? = emptyList()
)