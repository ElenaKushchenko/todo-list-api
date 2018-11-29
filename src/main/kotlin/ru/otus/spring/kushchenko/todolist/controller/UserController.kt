package ru.otus.spring.kushchenko.todolist.controller

import org.springframework.web.bind.annotation.*
import ru.otus.spring.kushchenko.todolist.controller.UserController.Companion.BASE_URL
import ru.otus.spring.kushchenko.todolist.model.User
import ru.otus.spring.kushchenko.todolist.service.UserService

/**
 * Created by Elena on Nov, 2018
 */
@RestController
@RequestMapping(BASE_URL)
class UserController(private val service: UserService) {
    companion object {
        const val BASE_URL = "/users"
    }

    @GetMapping
    fun getAll(): List<User> =
        service.getAll()

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): User =
        service.get(id)

    @PostMapping
    fun create(@RequestBody user: User): String =
        service.create(user)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody user: User
    ) {
        service.update(user.copy(id = id))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String) =
        service.delete(id)

}