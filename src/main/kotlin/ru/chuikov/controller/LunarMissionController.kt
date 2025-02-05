package ru.chuikov.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.controller.util.Validator

@RestController
class LunarMissionController(
    val validator: Validator
) {

    @PostMapping
    fun newMission():ResponseEntity<out Any>{

    }


}