package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import ru.chuikov.checkPrefix

@Entity
@Table(name = "crew_entity")
class CrewEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "role")
    var role: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "spacecraft_entity_id")
    @JsonBackReference
    var spacecraft: SpacecraftEntity? = null


    override fun check(prefix:String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String,Array<String>>()
        checkEmpty(name,"${pre}name")?.let { a.putAll(it) }
        checkEmpty(role,"${pre}role")?.let { a.putAll(it) }
        return a
    }
}