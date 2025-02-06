package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "crew_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class CrewEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore(true)
    var id: Long? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "role")
    var role: String? = null

    @ManyToOne
    @JoinColumn(name = "spacecraft_entity_id")
    var spacecraft: SpacecraftEntity? = null
    override fun check(): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String,Array<String>>()
        checkEmpty(name,"crew_name")?.let { a.putAll(it) }
        checkEmpty(role,"crew_role")?.let { a.putAll(it) }
        return a
    }
}