package ru.chuikov.controller

import com.google.gson.annotations.SerializedName
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.repository.UserRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


@RestController
class UserController(
    val userRepository: UserRepository
) {

    data class UserRegistrationDto(
        @SerializedName("birth_date")
        var birth_date: String?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("first_name")
        var first_name: String?,
        @SerializedName("last_name")
        var last_name: String?,
        @SerializedName("password")
        var password: String?,
        @SerializedName("patronymic")
        var patronymic: String?
    ) {
    }

    @PostMapping("/registration")
    fun registration(@RequestBody request: UserRegistrationDto) {
//        var error = ErrorRegistrationResponse(
//            ErrorRegistrationResponse
//                .Error(
//                    code = 422,
//                    message = "Validation error",
//                    errors = emptyMap<String,Array<String>>()
//                    ))

        var errors = mutableMapOf<String, Array<String>>()

        //check names and patronomic
        when (request.first_name) {
            "" -> errors.put("first_name", arrayOf("field phone can not be blank"))
            null -> errors.put("first_name", arrayOf("field first_name empty"))
            request.first_name!!.lowercase() -> errors.put(
                "first_name",
                arrayOf("field first_name must start with a capital letter")
            )
        }
        when (request.last_name) {
            "" -> errors.put("last_name", arrayOf("field phone can not be blank"))
            null -> errors.put("last_name", arrayOf("field last_name empty"))
            request.last_name!!.lowercase() -> errors.put(
                "last_name",
                arrayOf("field last_name must start with a capital letter")
            )
        }
        when (request.patronymic) {
            "" -> errors.put("patronymic", arrayOf("field phone can not be blank"))
            null -> errors.put("patronymic", arrayOf("field patronymic empty"))
            request.patronymic!!.lowercase() -> errors.put(
                "patronymic",
                arrayOf("field patronymic must start with a capital letter")
            )
        }
        //check email
        if (request.email == null || request.email.equals(""))
            errors.put("email", arrayOf("field email can not be blank"))

        if (userRepository.existsByEmail(request.email!!))
            errors.put("email", arrayOf("field email already exists"))

        //check birth
        if (request.birth_date == null || request.birth_date.equals(""))
            errors.put("birth_date", arrayOf("field birth_date can not be blank"))

        //check password
        if ((request.password == null) || request.password.equals(""))
            errors.put("password", arrayOf("field password can not be blank"))




    }

    fun exist(s:String):Boolean{

        for (i in 'a'..'z'){
            if (notExist(s,i))
        }
    }
    fun notExist(s:String,ch:Char) = !s.contains(ch)

    data class ErrorRegistrationResponse(
        @SerializedName("error")
        var error: Error?
    ) {
        data class Error(
            @SerializedName("code")
            var code: Int?,
            @SerializedName("errors")
            var errors: Map<String, Array<String>>,
            @SerializedName("message")
            var message: String?
        ) {

        }
    }


}

