package ru.chuikov.controller.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


val LOGIN_FAILED = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
    mapOf("message" to "Login failed")
)

val LOGIN_FORBIDDEN = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
    mapOf("message" to "Forbidden for you")
)

val LOGIN_NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    mapOf(
        "message" to "Not found",
        "code" to 404
    )
)
