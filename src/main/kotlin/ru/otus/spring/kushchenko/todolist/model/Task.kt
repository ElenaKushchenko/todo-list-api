package ru.otus.spring.kushchenko.todolist.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * Created by Elena on Nov, 2018
 */
data class Task(
    val text: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    val deadline: LocalDateTime?,
    val status: TaskStatus,
    val order: Int
)