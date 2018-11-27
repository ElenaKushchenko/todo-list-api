package ru.otus.spring.kushchenko.todolist.service

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject
import ru.otus.spring.kushchenko.todolist.model.Task
import ru.otus.spring.kushchenko.todolist.model.TaskStatus
import ru.otus.spring.kushchenko.todolist.repository.ProjectRepository
import java.time.LocalDate
import java.util.*

/**
 * Created by Elena on Nov, 2018
 */
class ProjectServiceImplTest {
    private val repository: ProjectRepository = mock()
    private val service = ProjectServiceImpl(repository)

    @AfterEach
    fun clean() {
        reset(repository)
    }

    @Test
    fun getAll() {
        val projects = listOf(
            ShortProject(
                id = "1",
                name = "Project1",
                order = 1
            ),
            ShortProject(
                id = "2",
                name = "Project2",
                order = 2
            )
        )

        whenever(repository.findAllShortProjects()).thenReturn(projects)

        assertEquals(projects, service.getAll())

        verify(repository).findAllShortProjects()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun getPaged() {
        val page = 1
        val size = 20
        val sortBy = "name"
        val dir = "ASC"

        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort(Sort.Direction.valueOf(dir), sortBy)
        )

        val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

        val projects = listOf(
            Project(
                id = "1",
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            ),
            Project(
                id = "2",
                name = "Project2",
                order = 2,
                tasks = listOf(task1, task2, task3)
            )
        )
        val projectsPage = PageImpl(projects, pageable, projects.size.toLong())

        whenever(repository.findAll(pageable)).thenReturn(projectsPage)

        assertEquals(projectsPage, service.getPaged(page, size, sortBy, dir))

        verify(repository).findAll(pageable)
        verifyNoMoreInteractions(repository)
    }

    @Nested
    @DisplayName("Tests for get() method")
    inner class Get {

        @Test
        fun shouldPassSuccessfully() {
            val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
            val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
            val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

            val projectId = "1"
            val project = Project(
                id = projectId,
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            )

            whenever(repository.findById(projectId)).thenReturn(Optional.of(project))

            assertEquals(project, service.get(projectId))

            verify(repository).findById(projectId)
            verifyNoMoreInteractions(repository)
        }

        @Test
        fun shouldFailBecauseProjectDoesNotExist() {
            val projectId = "100500"

            whenever(repository.findById(projectId)).thenReturn(Optional.empty())

            Assertions.assertThatThrownBy { service.get(projectId) }
                .isInstanceOf(IllegalArgumentException::class.java)

            verify(repository).findById(projectId)
            verifyNoMoreInteractions(repository)
        }
    }

    @Nested
    @DisplayName("Tests for create() method")
    inner class Create {

        @Test
        fun shouldPassSuccessfully() {
            val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
            val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
            val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

            val projectId = "1"
            val project = Project(
                id = projectId,
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            )

            whenever(repository.existsById(projectId)).thenReturn(false)
            whenever(repository.save(project)).thenReturn(project)

            assertEquals(project, service.create(project))

            verify(repository).existsById(projectId)
            verify(repository).save(project)
            verifyNoMoreInteractions(repository)
        }

        @Test
        fun shouldFailBecauseProjectAlreadyExists() {
            val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
            val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
            val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

            val projectId = "1"
            val project = Project(
                id = projectId,
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            )

            whenever(repository.existsById(projectId)).thenReturn(true)

            Assertions.assertThatThrownBy { service.create(project) }
                .isInstanceOf(IllegalArgumentException::class.java)

            verify(repository).existsById(projectId)
            verifyNoMoreInteractions(repository)
        }
    }

    @Nested
    @DisplayName("Tests for update() method")
    inner class Update {

        @Test
        fun shouldPassSuccessfully() {
            val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
            val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
            val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

            val projectId = "1"
            val project = Project(
                id = projectId,
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            )

            whenever(repository.existsById(projectId)).thenReturn(true)
            whenever(repository.save(project)).thenReturn(project)

            assertEquals(project, service.update(project))

            verify(repository).existsById(projectId)
            verify(repository).save(project)
            verifyNoMoreInteractions(repository)
        }

        @Test
        fun shouldFailBecauseProjectDoesNotExist() {
            val task1 = Task("Text1", LocalDate.now(), TaskStatus.TO_DO, 1)
            val task2 = Task("Text2", LocalDate.now(), TaskStatus.IN_PROGRESS, 2)
            val task3 = Task("Text3", LocalDate.now(), TaskStatus.DONE, 3)

            val projectId = "1"
            val project = Project(
                id = projectId,
                name = "Project1",
                order = 1,
                tasks = listOf(task1, task2, task3)
            )

            whenever(repository.existsById(projectId)).thenReturn(false)

            Assertions.assertThatThrownBy { service.update(project) }
                .isInstanceOf(IllegalArgumentException::class.java)

            verify(repository).existsById(projectId)
            verifyNoMoreInteractions(repository)
        }

        @Test
        fun shouldFailBecauseProjectIdNotSpecified() {
            val project = Project(
                name = "Project1",
                order = 1
            )

            Assertions.assertThatThrownBy { service.update(project) }
                .isInstanceOf(NullPointerException::class.java)

            verifyNoMoreInteractions(repository)
        }
    }

    @Test
    fun delete() {
        val projectId = "1"

        doNothing().whenever(repository).deleteById(projectId)

        service.delete(projectId)

        verify(repository).deleteById(projectId)
        verifyNoMoreInteractions(repository)
    }
}