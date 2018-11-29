package ru.otus.spring.kushchenko.todolist.controller.security

import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.otus.spring.kushchenko.todolist.service.security.JwtTokenProcessor
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Elena on Nov, 2018
 */
@Component
class AuthorizationFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtTokenProcessor: JwtTokenProcessor
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(AuthorizationFilter::class.java)

    private val AUTH_TYPE = "Bearer "
    @Value("\${jwt.header}")
    private val tokenHeader: String = ""

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestHeader = request.getHeader(this.tokenHeader)

        var username: String? = null
        var token: String? = null

        if (true == requestHeader?.startsWith(AUTH_TYPE)) {
            token = requestHeader.substringAfter(AUTH_TYPE)

            try {
                username = jwtTokenProcessor.getUsernameFromToken(token)
            } catch (e: IllegalArgumentException) {
                log.error("Error occurred while getting username from token", e)
            } catch (e: ExpiredJwtException) {
                log.warn("Token is expired", e)
            }
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            if (jwtTokenProcessor.validateToken(token!!)) {
                val userDetails = this.userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                logger.info("User $username has been authorized")

                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        chain.doFilter(request, response)
    }
}