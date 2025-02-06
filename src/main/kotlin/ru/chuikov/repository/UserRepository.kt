package ru.chuikov.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.chuikov.entity.User
import ru.chuikov.entity.mission.MissionEntity
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Int> {



    fun findByToken(token: String): User?
    @Transactional
    @Modifying
    @Query("update User u set u.token = ?1 where u.id = ?2")
    fun updateToken(token: String?, id: Int)

//
//    fun findByToken(token: String): User?
//
//    @Transactional
//    @Modifying
//    @Query("update User u set u.token = 0 where u.id = ?1")
//    fun logoutUser(id: Int)
//
//    @Transactional
//    @Modifying
//    @Query("update User u set u.token = ?1 where u.id = ?2")
//    fun updateTokenById(token: String, id: Int)

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?



}