package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "landing_site_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class LandingSiteEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore(true)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "coordinates_entity_id")
    var coordinates: CoordinatesEntity? = null

    @Column(name = "name")
    var name: String? = null


    override fun check(): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(name, "landing_site_name")?.let { a.putAll(it) }
        coordinates?.let {
            it.check()?.let {
                a.putAll(it)
            }
        }

        return a
    }
}