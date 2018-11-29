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
class ProjectServiceImpl(private val repository: ProjectRepository) : ProjectService {
    private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)

    override fun getAll(sortBy: String, dir: String): List<ShortProject> {
        val sort = Sort(Sort.Direction.valueOf(dir), sortBy)

        return repository.findAllShortProjects(sort)
    }

    override fun getPaged(page: Int, size: Int, sortBy: String, dir: String): Page<Project> {
        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort(Sort.Direction.valueOf(dir), sortBy)
        )

        return repository.findAll(pageable)
    }

    override fun get(id: String): Project = repository.findById(id)
        .orElseThrow { IllegalArgumentException("Project with id = $id not found") }

    override fun create(project: Project): String {
        project.id?.let {
            if (repository.existsById(it))
                throw IllegalArgumentException("Project with id = $it already exists")
        }

        return repository.save(project).id!!
    }

    override fun update(projects: List<Project>) {
        val idsToUpdate = projects.map { it.id }
        val toUpdate = repository.findAllById(idsToUpdate).associateBy { it.id }

        if (idsToUpdate.size > toUpdate.size) {
            val missedIds = idsToUpdate.minus(toUpdate.keys)
            throw IllegalArgumentException("Projects with ids = $missedIds not found")
        }

        val updated = projects.map {
            toUpdate[it.id]!!
                .copy(
                    name = it.name,
                    order = it.order
                )
        }
        repository.saveAll(updated)
    }

    override fun update(project: Project) {
        val id = project.id!!

        if (repository.existsById(id).not())
            throw IllegalArgumentException("Project with id = $id not found")

        repository.save(project)
    }

    override fun delete(id: String) = repository.deleteById(id)
}