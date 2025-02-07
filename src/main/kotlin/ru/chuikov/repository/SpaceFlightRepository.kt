package ru.chuikov.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.chuikov.entity.SpaceFlightEntity
@Repository
interface SpaceFlightRepository : JpaRepository<SpaceFlightEntity, Long> {
}