package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ru.chuikov.checkPrefix

@Entity
@Table(name = "landing_site_entity")
class LandingSiteEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "coordinates_entity_id")
    @JsonManagedReference
    var coordinates: CoordinatesEntity? = null

    @Column(name = "name")
    var name: String? = null


    override fun check(prefix: String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(name, "${pre}name")?.let { a.putAll(it) }
        if (coordinates==null) a.put("${pre}coordinates", arrayOf("field coordinates can not be blank"))
        coordinates?.let {
            it.check("landing_site")?.let {
                a.putAll(it)
            }
        }

        return a
    }
}