package ru.otus.spring.kushchenko.todolist.service

import ru.otus.spring.kushchenko.todolist.model.User

/**
 * Created by Elena on Nov, 2018
 */
interface UserService {
    fun getAll(): List<User>
    fun get(id: String): User
    fun create(user: User): User
    fun update(user: User): User
    fun delete(id: String)
}