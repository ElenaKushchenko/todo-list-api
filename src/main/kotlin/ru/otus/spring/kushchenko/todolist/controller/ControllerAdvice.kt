package ru.otus.spring.kushchenko.todolist.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Elena on Nov, 2018
 */
@ControllerAdvice(annotations = [RestController::class])
class ControllerAdvice {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    private fun generalError(ex: Exception) =
        mapOf(
            "message" to if (ex.message.isNullOrBlank()) "Unknown Error" else ex.message,
            "type" to ex.javaClass.simpleName
        )
}