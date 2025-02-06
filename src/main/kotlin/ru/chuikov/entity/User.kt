package ru.chuikov.entity

import jakarta.persistence.*
import ru.chuikov.entity.mission.MissionEntity


@Entity
@Table(name = "user_account")
class User(
    birthDate: String,
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    patronymic: String,
    id: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,)
    //@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    val id: Int = id

    val birthDate: String = birthDate
    @Column(unique = true)
    val email: String = email
    val firstName: String = firstName
    val lastName: String = lastName
    val password: String = password
    val patronymic: String = patronymic
    var token: String?=null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var missions: MutableList<MissionEntity>? = null

    fun getFullName()="$firstName $lastName $patronymic"
}





