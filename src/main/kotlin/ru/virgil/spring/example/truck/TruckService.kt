package ru.virgil.spring.example.truck

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.order.BuyingOrder
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class TruckService(private val repository: TruckRepository) {

    fun get(uuid: UUID) = repository.findById(uuid).getOrNull()

    fun getAll(buyingOrder: BuyingOrder, page: Int, size: Int): List<Truck> =
        repository.findAllByBuyingOrderContaining(buyingOrder, PageRequest.of(page, size))

    fun assignTruck() = repository.findAll().randomOrNull()
}
