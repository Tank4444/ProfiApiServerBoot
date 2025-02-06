package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "spacecraft_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class SpacecraftEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore(true)
    var id: Long? = null

    @Column(name = "command_module")
    var commandModule: String? = null

    @OneToMany(mappedBy = "spacecraft", orphanRemoval = true)
    var crew: MutableSet<CrewEntity>? = null

    @Column(name = "lunar_module")
    var lunarModule: String? = null
    override fun check(): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(commandModule, "spacecraft_command_module")?.let { a.putAll(it) }
        checkEmpty(lunarModule, "spacecraft_lunar_module")?.let { a.putAll(it) }
        crew?.let {
            it.forEachIndexed { index, it ->
                it.check()?.let {
                    var n = mutableMapOf<String, Array<String>>()
                    it.forEach { t, u ->
                        n.put("${t}_$index", u)
                    }
                    a.putAll(n)
                }
            }
        }

        return a
    }
}