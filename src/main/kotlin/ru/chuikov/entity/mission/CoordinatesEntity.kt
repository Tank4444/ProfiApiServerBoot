package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "coordinates_entity")
class CoordinatesEntity:CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "latitude")
    var latitude: Double? = null

    @Column(name = "longitude")
    var longitude: Double? = null
    override fun check(): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String,Array<String>>()
        checkEmpty(latitude,"coordinates_latitude")?.let { a.putAll(it) }
        checkEmpty(longitude,"coordinates_longitude")?.let { a.putAll(it) }
        return a
    }
}