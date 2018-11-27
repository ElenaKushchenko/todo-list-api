package ru.otus.spring.kushchenko.todolist.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject

/**
 * Created by Elena on Nov, 2018
 */
interface ProjectRepository : MongoRepository<Project, String> {
    @Query("{}")
    fun findAllShortProjects(sort: Sort): List<ShortProject>
}