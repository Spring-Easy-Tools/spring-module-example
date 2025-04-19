package ru.virgil.spring.example.box

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security
import java.util.*

@Repository
interface BoxRepository : OwnedRepository<Box> {

    fun findByCreatedByAndUuidAndDeletedIsFalse(
        createdBy: String = Security.getSimpleCreator(),
        uuid: UUID,
    ): Optional<Box>

    fun findAllByCreatedByAndDeletedIsFalse(
        createdBy: String = Security.getSimpleCreator(),
        pageable: Pageable,
    ): List<Box>

    fun findAllByCreatedByAndTruckAndDeletedIsFalse(
        createdBy: String = Security.getSimpleCreator(),
        truck: Truck,
        pageable: Pageable,
    ): List<Box>

    fun countAllByCreatedByAndDeletedIsFalse(createdBy: String = Security.getSimpleCreator()): Long

    fun findAllByCreatedByAndTypeAndDeletedIsFalse(
        createdBy: String = Security.getSimpleCreator(),
        boxType: BoxType,
    ): List<Box>
}
