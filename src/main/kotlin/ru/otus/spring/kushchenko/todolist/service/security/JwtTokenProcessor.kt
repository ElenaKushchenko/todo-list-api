package ru.otus.spring.kushchenko.todolist.service.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by Elena on Nov, 2018
 */
@Component
class JwtTokenProcessor {
    private val clock = DefaultClock.INSTANCE

    @Value("\${jwt.secret}")
    private val secret: String = ""

    @Value("\${jwt.expiration}")
    private val expiration: Long = 0L

    fun generateToken(username: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    fun getUsernameFromToken(token: String): String =
        getClaimFromToken(token) { it -> it.subject }

    fun getExpirationDateFromToken(token: String): Date =
        getClaimFromToken(token) { it -> it.expiration }

    private fun <T> getClaimFromToken(token: String, handler: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return handler.invoke(claims)
    }

    private fun getAllClaimsFromToken(token: String) =
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body

    private fun isTokenExpired(token: String) =
        getExpirationDateFromToken(token).before(clock.now())

    fun canTokenBeRefreshed(token: String): Boolean = isTokenExpired(token).not()

    fun refreshToken(token: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        val claims = getAllClaimsFromToken(token)
            .apply {
                issuedAt = createdDate
                expiration = expirationDate
            }

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    private fun calculateExpirationDate(createdDate: Date): Date =
        Date(createdDate.time + expiration * 1000)

    fun validateToken(token: String): Boolean =
        isTokenExpired(token).not()
}