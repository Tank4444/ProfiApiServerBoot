package ru.chuikov.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.chuikov.entity.SpaceFlightEntity

@Repository
interface SpaceFlightRepository : JpaRepository<SpaceFlightEntity, Long> {

    @Query("select s from SpaceFlightEntity s where s.id =:flight_number")
    fun findByFlightNumber(flight_number: String): SpaceFlightEntity?

    fun findByUser_Id(id: Int): List<SpaceFlightEntity>
}