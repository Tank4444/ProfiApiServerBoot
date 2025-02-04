package ru.chuikov.controller


import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.controller.util.LOGIN_FAILED
import ru.chuikov.controller.util.checkEmpty
import ru.chuikov.controller.util.checkPassword
import ru.chuikov.controller.util.validateStringWithFirstChar
import ru.chuikov.entity.User
import ru.chuikov.repository.UserRepository
import ru.chuikov.utils.JwtService


@RestController
class UserController(
    val userRepository: UserRepository,
    val tokenService: JwtService
) {


    @PostMapping("/registration")
    fun registration(@RequestBody request: UserRegistrationDto): ResponseEntity<Any> {
        var errors = mutableMapOf<String, Array<String>>()

        //check names
        validateStringWithFirstChar(request.first_name, "first_name")?.let { errors.putAll(it) }
        validateStringWithFirstChar(request.last_name, "last_name")?.let { errors.putAll(it) }
        validateStringWithFirstChar(request.patronymic, "patronymic")?.let { errors.putAll(it) }

        //check email
        if (request.email == null || request.email.equals(""))
            errors.put("email", arrayOf("field email can not be blank"))
        else if (userRepository.existsByEmail(request.email!!))
            errors.put("email", arrayOf("field email already exists"))
        //check birth
        checkEmpty(request.birth_date, "birth_date")?.let { errors.putAll(it) }

        checkPassword(request.password)?.let { errors.putAll(it) }
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
            return ResponseEntity.ok().body(
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
            return ResponseEntity.ok().body(
                ValidationErrorResponse(
                    ValidationErrorResponse.Error(
                        code = 422,
                        message = "Validation error",
                        errors = errors
                    )
                )
            )
        }
    }

    @PostMapping("/authorization")
    fun authorization(@RequestBody request: AuthorizationRequest): ResponseEntity<out Any> {
        var errors = mutableMapOf<String, Array<String>>()
        checkPassword(request.password)?.let { errors.putAll(it) }

        //check email
        if (request.email == null || request.email == "")
            errors.put("email", arrayOf("field email can not be blank"))
//        else if (userRepository.existsByEmail(request.email))
//            errors.put("email", arrayOf("field email already exists"))

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

        } else return ResponseEntity.ok().body(
            ValidationErrorResponse(
                ValidationErrorResponse.Error(
                    code = 422,
                    message = "Validation error",
                    errors = errors
                )
            )
        )
    }


}

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












