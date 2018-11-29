package ru.otus.spring.kushchenko.todolist.service.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
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

    fun generateToken(userDetails: UserDetails): String {
        return doGenerateToken(null, userDetails.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>?, subject: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    fun getUsernameFromToken(token: String): String =
        getClaimFromToken(token) { it -> it.subject }

    fun getIssuedAtDateFromToken(token: String): Date =
        getClaimFromToken(token) { it -> it.issuedAt }

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

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean =
        true == lastPasswordReset?.let { created.before(lastPasswordReset) }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean {
        val created = getIssuedAtDateFromToken(token)
        return isTokenExpired(token).not()
                && isCreatedBeforeLastPasswordReset(created, lastPasswordReset).not()
    }

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

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        userDetails as CustomUserDetails

        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)

        return (username == userDetails.username
                && isTokenExpired(token).not()
                && isCreatedBeforeLastPasswordReset(created, userDetails.lastPasswordResetDate)).not()
    }
}