package ru.otus.spring.kushchenko.todolist.controller

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject
import ru.otus.spring.kushchenko.todolist.model.Task
import ru.otus.spring.kushchenko.todolist.model.TaskStatus
import ru.otus.spring.kushchenko.todolist.service.ProjectService
import ru.otus.spring.kushchenko.todolist.util.Util.asJsonString
import java.time.LocalDateTime

/**
 * Created by Elena on Nov, 2018
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(ProjectController::class)
class ProjectControllerTest {
    private val BASE_URL = ProjectController.BASE_URL

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var service: ProjectService

    @AfterEach
    fun clean() {
        reset(service)
    }

    @Test
    fun getAll() {
        val sortBy = "name"
        val dir = "ASC"

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

        whenever(service.getAll(sortBy, dir)).thenReturn(projects)

        mockMvc.perform(
            get(BASE_URL)
                .param("sortBy", sortBy)
                .param("dir", dir)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().json(projects.asJsonString()))
            .andReturn().response

        verify(service).getAll(sortBy, dir)
        verifyNoMoreInteractions(service)
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

        val task1 = Task("Text1", LocalDateTime.now().withNano(0), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDateTime.now().withNano(0), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDateTime.now().withNano(0), TaskStatus.DONE, 3)

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

        whenever(service.getPaged(page, size, sortBy, dir)).thenReturn(projectsPage)

        mockMvc.perform(
            get("$BASE_URL/paged")
                .param("page", page.toString())
                .param("size", size.toString())
                .param("sortBy", sortBy)
                .param("dir", dir)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().json(projectsPage.asJsonString()))
            .andReturn().response

        verify(service).getPaged(page, size, sortBy, dir)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun get() {
        val task1 = Task("Text1", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.DONE, 3)

        val projectId = "1"
        val project = Project(
            id = projectId,
            name = "Project1",
            order = 1,
            tasks = listOf(task1, task2, task3)
        )

        whenever(service.get(projectId)).thenReturn(project)

        mockMvc.perform(get("$BASE_URL/{id}", projectId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().json(project.asJsonString()))
            .andReturn().response

        verify(service).get(projectId)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun create() {
        val task1 = Task("Text1", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.DONE, 3)

        val projectId = "1"
        val project = Project(
            id = projectId,
            name = "Project1",
            order = 1,
            tasks = listOf(task1, task2, task3)
        )

        whenever(service.create(project)).thenReturn(projectId)

        mockMvc.perform(
            post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(project.asJsonString())
        ).andDo(::print)
            .andExpect(status().isOk)
            .andExpect(content().string(projectId))
            .andReturn().response

        verify(service).create(project)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun updateAll() {
        val task1 = Task("Text1", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.DONE, 3)

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

        doNothing().whenever(service).update(projects)

        mockMvc.perform(
            put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projects.asJsonString())
        ).andDo(::print)
            .andExpect(status().isOk)

        verify(service).update(projects)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun update() {
        val task1 = Task("Text1", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.TO_DO, 1)
        val task2 = Task("Text2", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.IN_PROGRESS, 2)
        val task3 = Task("Text3", LocalDateTime.now().withSecond(0).withNano(0), TaskStatus.DONE, 3)

        val projectId = "1"
        val project = Project(
            id = projectId,
            name = "Project1",
            order = 1,
            tasks = listOf(task1, task2, task3)
        )

        doNothing().whenever(service).update(project)

        mockMvc.perform(
            put("$BASE_URL/{id}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(project.asJsonString())
        ).andDo(::print)
            .andExpect(status().isOk)

        verify(service).update(project)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun delete() {
        val bookId = "1"

        doNothing().whenever(service).delete(bookId)
        mockMvc.perform(
            delete("$BASE_URL/{id}", bookId)
        )
            .andExpect(status().isOk)

        verify(service).delete(bookId)
        verifyNoMoreInteractions(service)
    }
}