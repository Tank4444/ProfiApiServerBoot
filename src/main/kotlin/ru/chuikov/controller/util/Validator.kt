package ru.chuikov.controller.util


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