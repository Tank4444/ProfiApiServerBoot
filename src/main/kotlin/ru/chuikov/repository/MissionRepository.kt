package ru.chuikov.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.chuikov.entity.mission.MissionEntity

@Repository
interface MissionRepository : JpaRepository<MissionEntity, Long>{
}