package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "launch_site_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class LaunchSiteEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "location_entity_id")
    var location: LocationEntity? = null

    @Column(name = "name")
    var name: String? = null
    override fun check(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }
}