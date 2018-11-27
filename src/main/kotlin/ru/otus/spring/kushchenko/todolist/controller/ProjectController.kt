package ru.otus.spring.kushchenko.todolist.controller

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.otus.spring.kushchenko.todolist.controller.ProjectController.Companion.BASE_URL
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject
import ru.otus.spring.kushchenko.todolist.service.ProjectService

/**
 * Created by Elena on Nov, 2018
 */
@RestController
@RequestMapping(BASE_URL)
class ProjectController(private val service: ProjectService) {
    companion object {
        const val BASE_URL = "/projects"
    }

    @GetMapping
    fun getAll(): List<ShortProject> =
        service.getAll()

    @GetMapping("/paged")
    fun getPaged(
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "20") size: Int,
        @RequestParam(value = "sortBy", required = false, defaultValue = "name") sortBy: String,
        @RequestParam(value = "dir", required = false, defaultValue = "ASC") dir: String
    ): Page<Project> =
        service.getPaged(page, size, sortBy, dir)

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): Project =
        service.get(id)

    @PostMapping
    fun create(@RequestBody project: Project): Project =
        service.create(project)

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: String,
        @RequestBody project: Project
    ): Project =
        service.update(project.copy(id = id))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String) =
        service.delete(id)
}