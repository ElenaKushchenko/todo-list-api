package ru.otus.spring.kushchenko.todolist.service

import org.springframework.data.domain.Page
import org.springframework.security.access.annotation.Secured
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject

/**
 * Created by Elena on Nov, 2018
 */
interface ProjectService {
    fun getAll(sortBy: String, dir: String): List<ShortProject>
    fun getPaged(page: Int, size: Int, sortBy: String, dir: String): Page<Project>
    fun get(id: String): Project
    fun create(project: Project): String
    fun update(projects: List<Project>)
    fun update(project: Project)
    fun delete(id: String)
}