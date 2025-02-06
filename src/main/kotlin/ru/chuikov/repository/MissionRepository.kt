package ru.chuikov.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.chuikov.entity.mission.MissionEntity
import java.util.*

@Repository
interface MissionRepository : JpaRepository<MissionEntity, Long>{
    override fun findById(id: Long): Optional<MissionEntity>
    fun findByUser_Id(id: Int): List<MissionEntity>
}