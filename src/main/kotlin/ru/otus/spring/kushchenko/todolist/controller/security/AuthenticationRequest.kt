package ru.otus.spring.kushchenko.todolist.controller.security

/**
 * Created by Elena on Nov, 2018
 */
data class AuthenticationRequest(
    var username: String? = null,
    var password: String? = null
)
