package ru.chuikov.entity.mission

interface CheckInterface {
    fun check():MutableMap<String,Array<String>>?

    fun checkEmpty(s: String?, field: String) = when (s) {
        "" -> mapOf(field to arrayOf("field $field can not be blank"))
        null -> mapOf(field to arrayOf("field $field can not be blank"))
        else -> null
    }
    fun checkEmpty(s: Double?, field: String) = when (s) {
        null -> mapOf(field to arrayOf("field $field can not be blank"))
        else -> null
    }
    fun checkEmpty(s: Int?, field: String) = when (s) {
        null -> mapOf(field to arrayOf("field $field can not be blank"))
        else -> null
    }
}