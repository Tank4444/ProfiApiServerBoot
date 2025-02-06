package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import ru.chuikov.checkPrefix

@Entity
@Table(name = "launch_details_entity")
class LaunchDetailsEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "launch_date")
    var launch_date: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "launch_site_entity_id")
    @JsonManagedReference
    var launch_site: LaunchSiteEntity? = null


    override fun check(prefix:String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(launch_date, "${pre}launch_date")?.let { a.putAll(it) }
        if (launch_site==null) a.put("${pre}launch_site", arrayOf("field launch_site can not be blank"))
        launch_site?.let {
            it.check("launch_details")?.let {
                a.putAll(it)
            }
        }

        return a

    }
}