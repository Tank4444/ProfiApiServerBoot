package ru.chuikov.controller

import com.google.gson.annotations.SerializedName
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.ServerRequest.Headers
import ru.chuikov.controller.util.LOGIN_FAILED
import ru.chuikov.controller.util.Validator
import ru.chuikov.entity.User
import ru.chuikov.entity.mission.MissionEntity
import ru.chuikov.repository.MissionRepository
import ru.chuikov.repository.UserRepository

@RestController
class LunarMissionController(
    val validator: Validator,
    val userRepository: UserRepository,
    val missionRepository: MissionRepository,
    val modelMapper: ModelMapper
) {

    @PostMapping("/lunar-missions")
    fun newMission(
        @RequestBody request: MissionAddRequest,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var user: User = userRepository.findByToken(token!!.split(" ")[1])!!
        var mission:MissionEntity = modelMapper.map(request,MissionEntity::class.java)

        println("dasfj")
        return ResponseEntity.ok().body(mission)

    }


    data class MissionAddRequest(
        @SerializedName("landing_details")
        var landingDetails: LandingDetails?,
        @SerializedName("launch_details")
        var launchDetails: LaunchDetails?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("spacecraft")
        var spacecraft: Spacecraft?
    ) {
        data class LandingDetails(
            @SerializedName("landing_date")
            var landingDate: String?,
            @SerializedName("landing_site")
            var landingSite: LandingSite?
        ) {
            data class LandingSite(
                @SerializedName("coordinates")
                var coordinates: Coordinates?,
                @SerializedName("name")
                var name: String?
            ) {
                data class Coordinates(
                    @SerializedName("latitude")
                    var latitude: Double?,
                    @SerializedName("longitude")
                    var longitude: Double?
                )
            }
        }

        data class LaunchDetails(
            @SerializedName("launch_date")
            var launchDate: String?,
            @SerializedName("launch_site")
            var launchSite: LaunchSite?
        ) {
            data class LaunchSite(
                @SerializedName("location")
                var location: Location?,
                @SerializedName("name")
                var name: String?
            ) {
                data class Location(
                    @SerializedName("latitude")
                    var latitude: Double?,
                    @SerializedName("longitude")
                    var longitude: Double?
                )
            }
        }

        data class Spacecraft(
            @SerializedName("command_module")
            var commandModule: String?,
            @SerializedName("crew")
            var crew: List<Crew?>?,
            @SerializedName("lunar_module")
            var lunarModule: String?
        ) {
            data class Crew(
                @SerializedName("name")
                var name: String?,
                @SerializedName("role")
                var role: String?
            )
        }
    }
}