package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import ru.chuikov.checkPrefix

@Entity
@Table(name = "launch_site_entity")
class LaunchSiteEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_entity_id")
    @JsonManagedReference
    var location: LocationEntity? = null

    @Column(name = "name")
    var name: String? = null


    override fun check(prefix:String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(name, "${pre}name")?.let { a.putAll(it) }
        if (location==null) a.put("${pre}location", arrayOf("field location can not be blank"))
        location?.let {
            it.check("location")?.let {
                a.putAll(it)
            }
        }

        return a
    }
}