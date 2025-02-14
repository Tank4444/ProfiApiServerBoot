package ru.chuikov.controller


import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.controller.util.*
import ru.chuikov.entity.User
import ru.chuikov.repository.UserRepository
import ru.chuikov.utils.JwtService


@RestController
class UserController(
    val userRepository: UserRepository,
    val tokenService: JwtService,
    val validator: Validator
) {
    @PostMapping("/registration")
    fun registration(@RequestBody request: UserRegistrationDto): ResponseEntity<out Any> {
        var errors = mutableMapOf<String, Array<String>>()


        validator.validateStringWithFirstChar(request.first_name, "first_name")?.let { errors.putAll(it) }
        validator.validateStringWithFirstChar(request.last_name, "last_name")?.let { errors.putAll(it) }
        validator.validateStringWithFirstChar(request.patronymic, "patronymic")?.let { errors.putAll(it) }
        validator.fullCheckEmail(request.email)?.let { errors.putAll(it) }
        validator.checkEmpty(request.birth_date, "birth_date")?.let { errors.putAll(it) }
        validator.checkPassword(request.password)?.let { errors.putAll(it) }

        if (errors.isEmpty()) {
            val user = User(
                id = 0,
                birthDate = request.birth_date!!,
                email = request.email!!,
                firstName = request.first_name!!,
                lastName = request.last_name!!,
                password = request.password!!,
                patronymic = request.patronymic!!,
            )
            userRepository.save(
                user
            )
            return ResponseEntity.status(201).body(
                RegistrationSuccessfulResponse(
                    data = RegistrationSuccessfulResponse.Data(
                        code = 201,
                        message = "Пользователь создан",
                        user = RegistrationSuccessfulResponse.Data.User(
                            email = user.email,
                            name = user.getFullName()
                        )
                    )
                )
            )
        } else {
            return ResponseEntity.status(422).body(
                getValidationErrorResponse(errors = errors)
            )
        }
    }

    @PostMapping("/authorization",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        )
    fun authorization(@RequestBody request: AuthorizationRequest): ResponseEntity<out Any> {
        var errors = mutableMapOf<String, Array<String>>()
        validator.checkPassword(request.password)?.let { errors.putAll(it) }

        //check email
        validator.lightCheckEmail(request.email)?.let { errors.putAll(it) }

        if (errors.isEmpty()) {
            var user: User = userRepository.findByEmail(request.email!!)
                ?: return LOGIN_FAILED
            if (user.password == request.password) {
                var token = tokenService.generateToken()
                user.token = token
                userRepository.updateToken(token, user.id)
                return ResponseEntity.ok().body(
                    AuthorizationSuccessfulResponse(
                        data = AuthorizationSuccessfulResponse.Data(
                            token = token,
                            user = AuthorizationSuccessfulResponse.User(
                                id = user.id,
                                name = user.getFullName(),
                                birthDate = user.birthDate,
                                email = user.email
                            )
                        )
                    )
                )
            }
            return LOGIN_FAILED
        } else return ResponseEntity.status(422).body(
            getValidationErrorResponse(errors = errors)
        )
    }

    @GetMapping("/logout")
    fun logout(@RequestHeader(value = "Authorization") token: String?): ResponseEntity<out Any> {
        if (validator.tokenIsValid(token)) {
            var user = userRepository.findByToken(token!!.split(" ")[1])
            userRepository.updateToken(null, user!!.id)
            return ResponseEntity.status(204).contentType(MediaType.APPLICATION_JSON).body(null)
        } else return LOGIN_FAILED
    }
}

@JsonSerialize
data class RegistrationSuccessfulResponse(
    @JsonProperty("data")
    var `data`: Data?
) {
    data class Data(
        @JsonProperty("code")
        var code: Int?,
        @JsonProperty("message")
        var message: String?,
        @JsonProperty("user")
        var user: User?
    ) {
        data class User(
            @JsonProperty("email")
            var email: String?,
            @JsonProperty("name")
            var name: String?
        )
    }
}

data class AuthorizationRequest(
    @JsonProperty("email")
    val email: String?,
    @JsonProperty("password")
    val password: String?
) {
}

data class AuthorizationSuccessfulResponse(
    @JsonProperty("data")
    val `data`: Data?
) {
    data class Data(
        @JsonProperty("token")
        val token: String?,
        @JsonProperty("user")
        val user: User?
    )

    data class User(
        @JsonProperty("birth_date")
        val birthDate: String?,
        @JsonProperty("email")
        val email: String?,
        @JsonProperty("id")
        val id: Int?,
        @JsonProperty("name")
        val name: String?
    )
}












