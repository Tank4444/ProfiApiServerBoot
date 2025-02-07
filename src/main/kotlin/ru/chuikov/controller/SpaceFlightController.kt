package ru.chuikov.controller

import com.google.gson.annotations.SerializedName
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.chuikov.controller.util.LOGIN_FAILED
import ru.chuikov.controller.util.Validator
import ru.chuikov.controller.util.getValidationErrorResponse
import ru.chuikov.entity.SpaceFlightEntity
import ru.chuikov.entity.User
import ru.chuikov.repository.SpaceFlightRepository
import ru.chuikov.repository.UserRepository


@RestController
class SpaceFlightController(
    val validator: Validator,
    val userRepository: UserRepository,
    val mapper: ModelMapper,
    val spaceFlightRepository: SpaceFlightRepository
) {


    @PostMapping("/space-flights")
    fun createFlight(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?,
        @RequestBody flight: SpaceFlightRequest
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var spFlight = mapper.map(flight, SpaceFlightEntity::class.java)
        var errors = spFlight.check("")?: mapOf<String,Array<String>>()
        if (errors.isNotEmpty()){
            return ResponseEntity.status(422).contentType(MediaType.APPLICATION_JSON).body(
                getValidationErrorResponse(errors = errors)
            )
        }
        var user: User = userRepository.findByToken(token!!) ?: return LOGIN_FAILED
        spFlight.user = user
        spaceFlightRepository.save(spFlight)
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(
            mapOf(
                "data" to mapOf(
                    "code" to 201,
                    "message" to "Космический полет создан"
                )
            )
        )

    }

    fun getFlights(@RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?): ResponseEntity<out Any> {
        TODO("")
    }

    fun bayTicket(@RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?): ResponseEntity<out Any> {
        TODO("")
    }

    fun search(@RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?): ResponseEntity<out Any> {
        TODO("")

    }

    data class SpaceFlightRequest(
        @SerializedName("destination")
        var destination: String?,
        @SerializedName("flight_number")
        var flight_number: String?,
        @SerializedName("launch_date")
        var launch_date: String?,
        @SerializedName("seats_available")
        var seats_available: Int?
    )


}







