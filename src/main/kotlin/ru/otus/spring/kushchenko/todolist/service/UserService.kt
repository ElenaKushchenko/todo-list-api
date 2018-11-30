package ru.otus.spring.kushchenko.todolist.service

import org.springframework.security.access.annotation.Secured
import ru.otus.spring.kushchenko.todolist.model.User

/**
 * Created by Elena on Nov, 2018
 */
interface UserService {
    @Secured("ADMIN")
    fun getAll(): List<User>
    @Secured("ADMIN")
    fun get(id: String): User
    fun create(user: User): String
    fun update(user: User)
    @Secured("ADMIN")
    fun delete(id: String)
}