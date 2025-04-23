package ru.virgil.spring.example.box

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security
import java.util.*

@Repository
interface BoxRepository : JpaRepository<Box, UUID> {

    fun findByCreatedByAndUuidAndDeletedIsFalse(
        createdBy: String = Security.getCreator(),
        uuid: UUID,
    ): Box?

    fun findAllByCreatedByAndDeletedIsFalse(
        createdBy: String = Security.getCreator(),
        pageable: Pageable,
    ): List<Box>

    fun findAllByCreatedByAndTruckAndDeletedIsFalse(
        createdBy: String = Security.getCreator(),
        truck: Truck,
        pageable: Pageable,
    ): List<Box>

    fun countAllByCreatedByAndDeletedIsFalse(createdBy: String = Security.getCreator()): Long

    fun findAllByCreatedByAndTypeAndDeletedIsFalse(
        createdBy: String = Security.getCreator(),
        boxType: BoxType,
    ): List<Box>
}
