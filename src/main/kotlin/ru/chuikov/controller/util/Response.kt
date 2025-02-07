package ru.chuikov.controller.util

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


val LOGIN_FAILED = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
    mapOf("message" to "Login failed")
)

val LOGIN_FORBIDDEN = ResponseEntity.status(HttpStatus.FORBIDDEN).body(
    mapOf("message" to "Forbidden for you")
)

val NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    mapOf(
        "message" to "Not found",
        "code" to 404
    )
)

fun getValidationErrorResponse(code: Int = 422, errors: Map<String, Array<String>>) = ValidationErrorResponse(
    ValidationErrorResponse.Error(
        code = code,
        message = "Validation error",
        errors = errors
    )
)

data class ValidationErrorResponse(
    @JsonProperty("error")
    var error: Error?
) {
    data class Error(
        @JsonProperty("code")
        var code: Int?,
        @JsonProperty("errors")
        var errors: Map<String, Array<String>>,
        @JsonProperty("message")
        var message: String?
    ) {
    }
}

data class UserRegistrationDto(
    @JsonProperty("birth_date")
    var birth_date: String?,
    @JsonProperty("email")
    var email: String?,
    @JsonProperty("first_name")
    var first_name: String?,
    @JsonProperty("last_name")
    var last_name: String?,
    @JsonProperty("password")
    var password: String?,
    @JsonProperty("patronymic")
    var patronymic: String?
)