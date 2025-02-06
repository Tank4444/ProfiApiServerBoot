package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "location_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class LocationEntity:CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "latitude")
    var latitude: Double? = null

    @Column(name = "longitude")
    var longitude: Double? = null
    override fun check(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }
}