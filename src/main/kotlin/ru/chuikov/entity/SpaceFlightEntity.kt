package ru.chuikov.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.chuikov.checkPrefix
import ru.chuikov.entity.mission.CheckInterface

@Entity
@Table(name = "space_flight_entity")
class SpaceFlightEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "destination")
    var destination: String? = null

    @Column(name = "flight_number")
    var flight_number: String? = null

    @Column(name = "launch_date")
    var launch_date: String? = null

    @Column(name = "seats_available")
    var seats_available: Int? = null

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null

    override fun check(prefix: String): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String, Array<String>>()
        val pre = prefix.checkPrefix()
        checkEmpty(destination, "${pre}destination")?.let { a.putAll(it) }
        checkEmpty(flight_number, "${pre}flight_number")?.let { a.putAll(it) }
        checkEmpty(launch_date, "${pre}launch_date")?.let { a.putAll(it) }
        checkEmpty(seats_available, "${pre}seats_available")?.let { a.putAll(it) }

        return a
    }
}