package ru.chuikov.controller

import com.google.gson.annotations.SerializedName
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.chuikov.controller.util.*
import ru.chuikov.entity.SpaceFlightEntity
import ru.chuikov.entity.User
import ru.chuikov.entity.mission.*
import ru.chuikov.getToken
import ru.chuikov.repository.MissionRepository
import ru.chuikov.repository.UserRepository
import kotlin.jvm.optionals.getOrNull

@RestController
class LunarMissionController(
    val validator: Validator,
    val userRepository: UserRepository,
    val missionRepository: MissionRepository,
    val modelMapper: ModelMapper
) {

    @PostMapping("/lunar-missions")
    fun newMission(
        @RequestBody request: MissionRequest,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var user: User = userRepository.findByToken(token!!.split(" ")[1])!!
        var mission: MissionEntity = modelMapper.map(request.mission, MissionEntity::class.java)
        mission.user = user
        var errors = mission.check("")
        if (!errors.isEmpty()) return ResponseEntity.status(422).body(
            getValidationErrorResponse(errors = errors)
        )

        missionRepository.save(mission)
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(
            mapOf("data" to mapOf("code" to 201, "message" to "Миссия добавлена"))
        )
    }

    @GetMapping("/lunar-missions")
    fun getLunarMissions(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var user: User = userRepository.findByToken(token!!.split(" ")[1])!!
        var list = missionRepository.findByUser_Id(user.id)
        var nlist = list.map { mapOf("mission" to it) }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(nlist)
    }

    @DeleteMapping("/lunar-missions/{id}")
    fun delMission(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?,
        @PathVariable id: Long
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        var user: User = userRepository.findByToken(token!!.getToken()!!)!!
        var mission: MissionEntity = missionRepository.findById(id).getOrNull() ?: return NOT_FOUND
        if (user.id != mission.user!!.id) LOGIN_FORBIDDEN
        user.missions!!.removeIf { it.id == id }
        //user.missions = user.missions?.filter { it.id!=id }?.toMutableList()
        userRepository.save(user)
        return ResponseEntity.status(204).contentType(MediaType.APPLICATION_JSON).body(null)

    }

    @PatchMapping(
        "/lunar-missions/{id}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun editMission(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?,
        @PathVariable id: Long,
        @RequestBody request: MissionRequest
    ): ResponseEntity<out Any> {
        //valid token
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        //get user
        var user: User = userRepository.findByToken(token!!.getToken()!!)!!
        //get mission
        var oldMission: MissionEntity = missionRepository.findById(id).getOrNull() ?: return NOT_FOUND
        //check access to mission
        if (user.id != oldMission.user!!.id) LOGIN_FORBIDDEN
        //validate request
        var nMission: MissionEntity = modelMapper.map(request.mission, MissionEntity::class.java)
        var errors = nMission.check("")
        if (!errors.isEmpty()) return ResponseEntity.status(422).body(
            getValidationErrorResponse(errors = errors)
        )
        oldMission.name = nMission.name
        oldMission.spacecraft = nMission.spacecraft
        oldMission.launch_details = nMission.launch_details
        oldMission.landing_details = nMission.landing_details
        missionRepository.save(oldMission)
        return ResponseEntity.ok().body(mapOf("data" to mapOf("code" to 200, "message" to "Миссия обновлена")))
    }

    @GetMapping(
        "/search",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun search(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) token: String?,
        @RequestParam query: String?
    ): ResponseEntity<out Any> {
        if (!validator.tokenIsValid(token)) return LOGIN_FAILED
        if (query == null) return ResponseEntity.status(422)
            .body(getValidationErrorResponse(errors = mapOf("query" to arrayOf("field query is blank"))))
        var user: User = userRepository.findByToken(token!!.getToken() ?: "") ?: return LOGIN_FAILED
        var missions = missionRepository.findByUser_Id(user.id)


        var resMissions = mutableListOf<MissionEntity>()
        missions.forEach {
            var added = false
            if (it.name!!.lowercase().contains(query.lowercase())) added = true
            it.spacecraft?.crew?.forEach {
                if (it.name!!.lowercase().contains(query.lowercase())) added = true
            }
            if (added) resMissions.add(it)
        }

        return ResponseEntity.status(200).body(mapOf("data" to resMissions.map {
            SearchItemResponse(
                type = "Миссия",
                name = it.name,
                landing_date = it.landing_details?.landing_date,
                launch_date = it.launch_details?.launch_date,
                landing_site = it.landing_details?.landing_site?.name,
                crew = it.spacecraft?.crew?.map {
                    SearchItemResponse.Crew(name = it.name, role = it.role)
                }
            )
        }))

    }

    data class SearchItemResponse(
        @SerializedName("crew")
        var crew: List<Crew?>?,
        @SerializedName("landing_date")
        var landing_date: String?,
        @SerializedName("landing_site")
        var landing_site: String?,
        @SerializedName("launch_date")
        var launch_date: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("type")
        var type: String?
    ) {
        data class Crew(
            @SerializedName("name")
            var name: String?,
            @SerializedName("role")
            var role: String?
        )
    }

    data class MissionRequest(
        @SerializedName("mission")
        var mission: MissionAddRequest?
    )

    data class MissionAddRequest(
        @SerializedName("landing_details")
        var landing_details: LandingDetails?,
        @SerializedName("launch_details")
        var launch_details: LaunchDetails?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("spacecraft")
        var spacecraft: Spacecraft?
    ) {
        data class LandingDetails(
            @SerializedName("landing_date")
            var landing_date: String?,
            @SerializedName("landing_site")
            var landing_site: LandingSite?
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
            var launch_date: String?,
            @SerializedName("launch_site")
            var launch_site: LaunchSite?
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
            var command_module: String?,
            @SerializedName("crew")
            var crew: List<Crew?>?,
            @SerializedName("lunar_module")
            var lunar_module: String?
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