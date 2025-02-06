package ru.chuikov.entity.mission

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@Table(name = "launch_details_entity")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
class LaunchDetailsEntity :CheckInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "launch_date")
    var launchDate: String? = null

    @ManyToOne
    @JoinColumn(name = "launch_site_entity_id")
    var launchSite: LaunchSiteEntity? = null
    override fun check(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }
}