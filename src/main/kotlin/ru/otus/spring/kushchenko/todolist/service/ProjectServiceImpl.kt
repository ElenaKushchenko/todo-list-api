package ru.otus.spring.kushchenko.todolist.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.otus.spring.kushchenko.todolist.model.Project
import ru.otus.spring.kushchenko.todolist.model.ShortProject
import ru.otus.spring.kushchenko.todolist.repository.ProjectRepository

/**
 * Created by Elena on Nov, 2018
 */
@Service
class ProjectServiceImpl(private val projectRepository: ProjectRepository) : ProjectService {
    private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)

    override fun getAll(): List<ShortProject> = projectRepository.findAllShortProjects()

    override fun getPaged(page: Int, size: Int, sortBy: String, dir: String): Page<Project> {
        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort(Sort.Direction.valueOf(dir), sortBy)
        )

        return projectRepository.findAll(pageable)
    }

    override fun get(id: String): Project = projectRepository.findById(id)
        .orElseThrow { IllegalArgumentException("Project with id = $id not found") }

    override fun create(project: Project): Project {
        project.id?.let {
            if (projectRepository.existsById(it))
                throw IllegalArgumentException("Project with id = $it already exists")
        }

        return projectRepository.save(project)
    }

    override fun update(project: Project): Project {
        val id = project.id!!

        if (projectRepository.existsById(id).not())
            throw IllegalArgumentException("Project with id = $id not found")

        return projectRepository.save(project)
    }

    override fun delete(id: String) = projectRepository.deleteById(id)
}