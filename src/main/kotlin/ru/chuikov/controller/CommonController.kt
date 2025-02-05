package ru.chuikov.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.controller.util.LOGIN_FAILED
import ru.chuikov.controller.util.Validator

@RestController
class CommonController(
    val validator: Validator
) {
    @GetMapping("/api/gagarin-flight")
    fun getGagarinFlight(@RequestHeader(value = "Authorization") token: String?): ResponseEntity<out Any> {
        if (validator.tokenIsValid(token))
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                """
            {"data":[{"mission":{"name":"Восток 1","launch_details":{"launch_date":"1961-04-12","launch_site":{"name":"Космодром Байконур","location":{"latitude":"45.9650000","longitude":"63.3050000"}}},"flight_duration":{"hours":1,"minutes":48},"spacecraft":{"name":"Восток 3KA","manufacturer":"OKB-1","crew_capacity":1}},"landing":{"date":"1961-04-12","site":{"name":"Смеловка","country":"СССР","coordinates":{"latitude":"51.2700000","longitude":"45.9970000"}},"details":{"parachute_landing":true,"impact_velocity_mps":7}},"cosmonaut":{"name":"Юрий Гагарин","birthdate":"1934-03-09","rank":"Старший лейтенант","bio":{"early_life":"Родился в Клушино, Россия.","career":"Отобран в отряд космонавтов в 1960 году...","post_flight":"Стал международным героем."}}}]}
            """.trimIndent()
            )
        else return LOGIN_FAILED
    }
    @GetMapping("/flight")
    fun getFlight(@RequestHeader(value = "Authorization") token: String?): ResponseEntity<out Any> {
        if (validator.tokenIsValid(token))
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                """
                    {"data":{"name":"Аполлон-11","crew_capacity":3,"cosmonaut":[{"name":"Нил Армстронг","role":"Командир"},{"name":"Базз Олдрин","role":"Пилот лунного модуля"},{"name":"Майкл Коллинз","role":"Пилот командного модуля"}],"launch_details":{"launch_date":"1969-07-16","launch_site":{"name":"Космический центр имени Кеннеди","latitude":"28.5721000","longitude":"-80.6480000"}},"landing_details":{"landing_date":"1969-07-20","landing_site":{"name":"Море спокойствия","latitude":"0.6740000","longitude":"23.4720000"}}}}
            """.trimIndent()
            )
        else return LOGIN_FAILED
    }


}