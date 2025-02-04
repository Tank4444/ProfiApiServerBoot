package ru.chuikov.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.chuikov.entity.User
import java.security.Key
import java.util.*
import java.util.function.Function


@Service
class JwtService {
    @Value("\${token.signing.key}")
    private val jwtSigningKey: String? = null


    private fun generateToken(user: User): String {
        return Jwts.builder().setSubject(user.email)
            //.setIssuedAt(Date(System.currentTimeMillis()))
            //.setExpiration(Date(System.currentTimeMillis() + 100000 * 60 * 24))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact()
    }

    /**
     * Проверка токена на просроченность
     *
     * @param token токен
     * @return true, если токен просрочен
     */
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    /**
     * Извлечение даты истечения токена
     *
     * @param token токен
     * @return дата истечения
     */
    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return данные
    </T> */
    private fun <T> extractClaim(token: String, claimsResolvers: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers.apply(claims)
    }

    /**
     * Извлечение всех данных из токена
     *
     * @param token токен
     * @return данные
     */
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
            .getBody()
    }

    /**
     * Получение ключа для подписи токена
     *
     * @return ключ
     */
    private fun getSigningKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(jwtSigningKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}