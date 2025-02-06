package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "spacecraft_entity")
class SpacecraftEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "command_module")
    var command_module: String? = null

    @OneToMany(mappedBy = "spacecraft", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var crew: MutableSet<CrewEntity> = mutableSetOf()

    @Column(name = "lunar_module")
    var lunar_module: String? = null


    override fun check(prefix:String): MutableMap<String, Array<String>>? {
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(command_module, "${prefix}_command_module")?.let { a.putAll(it) }
        checkEmpty(lunar_module, "${prefix}_lunar_module")?.let { a.putAll(it) }
        if (crew==null||crew!!.isEmpty()) {
            a.put("spacecraft_crew",arrayOf("field crew can not be blank"))
        }else{
            crew!!.forEachIndexed { index, it ->
                it.check("spacecraft")?.let {
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