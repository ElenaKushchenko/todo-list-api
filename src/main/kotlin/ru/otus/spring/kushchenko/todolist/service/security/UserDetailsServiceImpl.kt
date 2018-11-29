package ru.otus.spring.kushchenko.todolist.service.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.otus.spring.kushchenko.todolist.model.User
import ru.otus.spring.kushchenko.todolist.repository.UserRepository

/**
 * Created by Elena on Nov, 2018
 */
@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username not exists")

        return user.toUserDetails()
    }

    private fun User.toUserDetails() =
        UserDetailsImpl(
            this.id!!,
            this.username,
            this.password,
            this.roles.map { authority -> SimpleGrantedAuthority(authority.name) },
            this.enabled
        )
}