package ru.virgil.spring.example.order

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security
import java.util.*

@Repository
interface BuyingOrderRepository : OwnedRepository<BuyingOrder> {

    fun findAllByCreatedBy(createdBy: String = Security.getSimpleCreator(), pageable: Pageable): List<BuyingOrder>

    fun findByCreatedByAndUuid(createdBy: String = Security.getSimpleCreator(), uuid: UUID): Optional<BuyingOrder>

    fun countAllByCreatedBy(createdBy: String = Security.getSimpleCreator()): Long

    fun findAllByTruck(truck: Truck, pageable: Pageable): List<BuyingOrder>
}
