package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ru.chuikov.checkPrefix
import ru.chuikov.entity.User

@Entity
@Table(name = "mission")
class MissionEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "landing_details_entity_id")
    @JsonManagedReference
    var landing_details: LandingDetailsEntity? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "launch_details_entity_id")
    @JsonManagedReference
    var launch_details: LaunchDetailsEntity? = null

    @Column(name = "name")
    var name: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "spacecraft_entity_id")
    @JsonManagedReference
    var spacecraft: SpacecraftEntity? = null


    override fun check(prefix: String): MutableMap<String, Array<String>> {

        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        //1 name
        checkEmpty(name, "${pre}name")?.let { a.putAll(it) }
        //2 launch
        if (launch_details == null) a.put("${pre}launch_details", arrayOf("field launch_details can not be blank"))
        launch_details?.let {
            it.check("launch_details")?.let {
                a.putAll(it)
            }
        }
        //3 landing
        if (landing_details == null) a.put("${pre}landing_details", arrayOf("field landing_details can not be blank"))
        landing_details?.let {
            it.check("landing_details")?.let {
                a.putAll(it)
            }
        }
        //4
        if (spacecraft == null) a.put("${pre}spacecraft", arrayOf("field spacecraft can not be blank"))
        spacecraft?.let {
            it.check("spacecraft")?.let {
                a.putAll(it)
            }
        }
        return a
    }
}
