package ru.otus.spring.kushchenko.todolist.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.otus.spring.kushchenko.todolist.model.User

/**
 * Created by Elena on Nov, 2018
 */
interface UserRepository : MongoRepository<User, String>