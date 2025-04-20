package ru.virgil.spring.example.order

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security
import java.util.*

@Repository
interface BuyingOrderRepository: JpaRepository<BuyingOrder, UUID> {

    fun findAllByCreatedBy(createdBy: String = Security.getSimpleCreator(), pageable: Pageable): List<BuyingOrder>

    fun findByCreatedByAndUuid(createdBy: String = Security.getSimpleCreator(), uuid: UUID): BuyingOrder?

    fun countAllByCreatedBy(createdBy: String = Security.getSimpleCreator()): Long

    fun findAllByTruck(truck: Truck, pageable: Pageable): List<BuyingOrder>
}
