package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.chuikov.entity.User

@Entity
@Table(name = "mission")
class MissionEntity:CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore(true)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    @JsonIgnore(true)
    var user: User? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "landing_details_entity_id")
    var landingDetails: LandingDetailsEntity? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "launch_details_entity_id")
    var launchDetails: LaunchDetailsEntity? = null

    @Column(name = "name")
    var name: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "spacecraft_entity_id")
    var spacecraft: SpacecraftEntity? = null


    override fun check(): MutableMap<String, Array<String>> {
        var res = mutableMapOf<String,Array<String>>();
        checkEmpty(name,"name")?.let { res.putAll(it) }
        TODO()

        return res
    }
}