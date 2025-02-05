package ru.chuikov.controller.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.chuikov.repository.UserRepository


fun getHeaderJson() = HttpHeaders().apply {
    contentType = MediaType.APPLICATION_JSON
}

@Service
class Validator(
    val userRepository: UserRepository
) {
    fun tokenIsValid(token:String?):Boolean{
        if (token == null || token == "") return false
        else {
            var tokenArr = token.split(" ")
            if (tokenArr.size == 2) {
                if (tokenArr[0] == "Bearer") {
                    var user = userRepository.findByToken(tokenArr[1])
                    if (user != null) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun fullCheckEmail(email: String?) =
        if (email == null || email == "")
            mapOf("email" to arrayOf("field email can not be blank"))
        else if (userRepository.existsByEmail(email))
            mapOf("email" to arrayOf("field email already exists"))
        else null

    fun lightCheckEmail(email: String?) =
        if (email == null || email == "")
            mapOf("email" to arrayOf("field email can not be blank"))
        else null

    fun checkPassword(s: String): Boolean {
        var sum = 0
        for (i in 'a'..'z') {
            if (exist(s, i)) {
                sum++
                break
            }
        }

        for (i in 'A'..'Z') {
            if (exist(s, i)) {
                sum++
                break
            }
        }

        for (i in '0'..'9') {
            if (exist(s, i)) {
                sum++
                break
            }
        }
        return sum == 3
    }

    fun exist(s: String, ch: Char) = s.contains(ch)

    fun validateStringWithFirstChar(s: String?, field: String) = when (s) {
        "" -> mapOf(field to arrayOf("field $field can not be blank"))
        null -> mapOf(field to arrayOf("field $field can not be blank"))
        s.lowercase() -> mapOf(field to arrayOf("field $field must start with a capital letter"))
        else -> null
    }

    fun checkEmpty(s: String?, field: String) = when (s) {
        "" -> mapOf(field to arrayOf("field $field can not be blank"))
        null -> mapOf(field to arrayOf("field $field can not be blank"))
        else -> null
    }

    fun checkPassword(pass: String?): Map<String, Array<String>>? {
        //check password
        if (pass == null || pass == "")
            return mapOf("password" to arrayOf("field password can not be blank"))
        if (!checkPassword(pass) || pass.length < 3)
            return mapOf("password" to arrayOf("field password does not meet the requirements"))

        return null
    }
}