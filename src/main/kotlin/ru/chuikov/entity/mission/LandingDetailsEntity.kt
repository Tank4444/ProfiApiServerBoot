package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "landing_details_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class LandingDetailsEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "landing_date")
    var landingDate: String? = null

    @ManyToOne
    @JoinColumn(name = "landing_site_entity_id")
    var landingSite: LandingSiteEntity? = null
    override fun check(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }
}