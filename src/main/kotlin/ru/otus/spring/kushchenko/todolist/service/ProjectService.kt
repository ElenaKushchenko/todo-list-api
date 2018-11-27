package ru.otus.spring.kushchenko.todolist.service

import org.springframework.data.domain.Page
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject

/**
 * Created by Elena on Nov, 2018
 */
interface ProjectService {
    fun getAll(): List<ShortProject>
    fun getPaged(page: Int, size: Int, sortBy: String, dir: String): Page<Project>
    fun get(id: String): Project
    fun create(project: Project): Project
    fun update(project: Project): Project
    fun delete(id: String)
}