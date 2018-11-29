//package ru.otus.spring.kushchenko.todolist.service
//
//import com.nhaarman.mockito_kotlin.*
//import org.assertj.core.api.Assertions
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import ru.otus.spring.kushchenko.todolist.model.User
//import ru.otus.spring.kushchenko.todolist.repository.UserRepository
//import java.lang.IllegalArgumentException
//import java.util.*
//
///**
// * Created by Elena on Nov, 2018
// */
//class UserServiceImplTest {
//    private val repository: UserRepository = mock()
//    private val service = UserServiceImpl(repository)
//
//    @AfterEach
//    fun clean() {
//        reset(repository)
//    }
//
//    @Test
//    fun getAll() {
//        val users = listOf(
//            User("1", "Reader1"),
//            User("2", "Reader2")
//        )
//
//        whenever(repository.findAll()).thenReturn(users)
//
//        assertEquals(users, service.getAll())
//
//        verify(repository).findAll()
//        verifyNoMoreInteractions(repository)
//    }
//
//    @Nested
//    @DisplayName("Tests for get() method")
//    inner class Get {
//
//        @Test
//        fun shouldPassSuccessfully() {
//            val userId = "1"
//            val user = User(userId, "Reader1")
//
//            whenever(repository.findById(userId)).thenReturn(Optional.of(user))
//
//            assertEquals(user, service.get(userId))
//
//            verify(repository).findById(userId)
//            verifyNoMoreInteractions(repository)
//        }
//
//        @Test
//        fun shouldFallBecauseUserDoesNotExist() {
//            val userId = "1"
//
//            whenever(repository.findById(userId)).thenReturn(Optional.empty())
//
//            Assertions.assertThatThrownBy { service.get(userId) }
//                .isInstanceOf(IllegalArgumentException::class.java)
//
//            verify(repository).findById(userId)
//            verifyNoMoreInteractions(repository)
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests for create() method")
//    inner class Create {
//
//        @Test
//        fun shouldPassSuccessfully() {
//            val userId = "1"
//            val user = User(userId, "Reader1")
//
//            whenever(repository.existsById(userId)).thenReturn(false)
//            whenever(repository.save(user)).thenReturn(user)
//
//            assertEquals(user.id, service.create(user))
//
//            verify(repository).existsById(userId)
//            verify(repository).save(user)
//            verifyNoMoreInteractions(repository)
//        }
//
//        @Test
//        fun shouldFailBecauseUserAlreadyExists() {
//            val userId = "1"
//            val user = User(userId, "Reader1")
//
//            whenever(repository.existsById(userId)).thenReturn(true)
//
//            Assertions.assertThatThrownBy { service.create(user) }
//                .isInstanceOf(IllegalArgumentException::class.java)
//
//            verify(repository).existsById(userId)
//            verifyNoMoreInteractions(repository)
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests for updateAll() method")
//    inner class Update {
//
//        @Test
//        fun shouldPassSuccessfully() {
//            val userId = "1"
//            val user = User(userId, "Reader1")
//
//            whenever(repository.existsById(userId)).thenReturn(true)
//            whenever(repository.save(user)).thenReturn(user)
//
//            service.update(user)
//
//            verify(repository).existsById(userId)
//            verify(repository).save(user)
//            verifyNoMoreInteractions(repository)
//        }
//
//        @Test
//        fun shouldFailBecauseUserDoesNotExist() {
//            val userId = "1"
//            val user = User(userId, "Reader1")
//
//            whenever(repository.existsById(userId)).thenReturn(false)
//
//            Assertions.assertThatThrownBy { service.update(user) }
//                .isInstanceOf(IllegalArgumentException::class.java)
//
//            verify(repository).existsById(userId)
//            verifyNoMoreInteractions(repository)
//        }
//
//        @Test
//        fun shouldFailBecauseUserIdNotSpecified() {
//            val user = User(null, "Reader1")
//
//            Assertions.assertThatThrownBy { service.update(user) }
//                .isInstanceOf(NullPointerException::class.java)
//
//            verifyNoMoreInteractions(repository)
//        }
//    }
//
//    @Test
//    fun delete() {
//        val userId = "1"
//
//        doNothing().whenever(repository).deleteById(userId)
//
//        service.delete(userId)
//
//        verify(repository).deleteById(userId)
//        verifyNoMoreInteractions(repository)
//    }
//}