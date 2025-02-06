package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.chuikov.checkPrefix

@Entity
@Table(name = "coordinates_entity")
class CoordinatesEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "latitude")
    var latitude: Double? = null

    @Column(name = "longitude")
    var longitude: Double? = null

    override fun check(prefix: String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(latitude, "${pre}latitude")?.let { a.putAll(it) }
        checkEmpty(longitude, "${pre}longitude")?.let { a.putAll(it) }
        return a
    }
}


