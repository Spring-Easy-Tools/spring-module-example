package ru.virgil.spring.example.box

import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import ru.virgil.spring.example.truck.Truck
import java.util.*

@Repository
interface BoxRepository : OwnedRepository<Box> {

    @Deprecated("Use Deleted False")
    fun findByCreatedByAndUuid(createdBy: UserDetails, uuid: UUID): Optional<Box>
    fun findByCreatedByAndUuidAndDeletedIsFalse(securityUser: UserDetails, uuid: UUID): Optional<Box>

    @Deprecated("Use Deleted False")
    fun findAllByCreatedBy(createdBy: UserDetails, pageable: Pageable): List<Box>
    fun findAllByCreatedByAndDeletedIsFalse(createdBy: UserDetails, pageable: Pageable): List<Box>

    @Deprecated("Use Deleted False")
    fun findAllByCreatedByAndTruck(createdBy: UserDetails, truck: Truck, pageable: Pageable): List<Box>
    fun findAllByCreatedByAndTruckAndDeletedIsFalse(createdBy: UserDetails, truck: Truck, pageable: Pageable): List<Box>

    @Deprecated("Use Deleted False")
    fun countAllByCreatedBy(createdBy: UserDetails): Long
    fun countAllByCreatedByAndDeletedIsFalse(createdBy: UserDetails): Long

    @Deprecated("Use Deleted False")
    fun findAllByCreatedByAndType(createdBy: UserDetails, boxType: BoxType): List<Box>
    fun findAllByCreatedByAndTypeAndDeletedIsFalse(createdBy: UserDetails, boxType: BoxType): List<Box>
}
