package ru.otus.spring.kushchenko.todolist.controller.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.otus.spring.kushchenko.todolist.service.security.CustomUserDetails
import ru.otus.spring.kushchenko.todolist.service.security.JwtTokenProcessor
import javax.servlet.http.HttpServletRequest

/**
 * Created by Elena on Nov, 2018
 */
@RestController
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProcessor: JwtTokenProcessor,
    private val userDetailsService: UserDetailsService
) {
    private val AUTH_TYPE = "Bearer "
    @Value("\${jwt.header}")
    private val tokenHeader: String = ""

    @PostMapping("/auth")
    fun createAuthenticationToken(@RequestBody auth: AuthenticationRequest): String {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(auth.username!!, auth.password!!))

        val userDetails = userDetailsService.loadUserByUsername(auth.username)
        return jwtTokenProcessor.generateToken(userDetails)
    }

    @GetMapping("/refresh")
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): String? {
        val authToken = request.getHeader(tokenHeader)
        val token = authToken.substringAfter(AUTH_TYPE)
        val username = jwtTokenProcessor.getUsernameFromToken(token)
        val user = userDetailsService.loadUserByUsername(username) as CustomUserDetails

        return if (jwtTokenProcessor.canTokenBeRefreshed(token, user.lastPasswordResetDate!!)) {
            jwtTokenProcessor.refreshToken(token)
        } else {
            null
        }
    }
}
