package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import org.modelmapper.internal.bytebuddy.build.ToStringPlugin.Enhance.Prefix
import ru.chuikov.checkPrefix

@Entity
@Table(name = "landing_details_entity")
class LandingDetailsEntity : CheckInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "landing_date")
    var landing_date: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "landing_site_entity_id")
    @JsonManagedReference
    var landing_site: LandingSiteEntity? = null

    override fun check(prefix: String): MutableMap<String, Array<String>>? {
        var pre = prefix.checkPrefix()
        var a = mutableMapOf<String, Array<String>>()
        checkEmpty(landing_date, "${pre}landing_date")?.let { a.putAll(it) }
        //checkEmpty(role,"landing_details_role")?.let { a.putAll(it) }
        if (landing_site == null) a.put("${pre}landing_site", arrayOf("field landing_site can not be blank"))
        landing_site!!.check("landing_details")?.let {
            a.putAll(it)
        }
        return a
    }
}