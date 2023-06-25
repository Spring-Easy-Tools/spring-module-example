package ru.virgil.spring.example.truck

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.order.BuyingOrder
import java.util.*

@Repository
interface TruckRepository : JpaRepository<Truck, UUID> {

    fun findAllByBuyingOrderContaining(buyingOrder: BuyingOrder, pageable: Pageable): List<Truck>
}
