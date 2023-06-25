package ru.virgil.spring.example.truck

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.order.BuyingOrder
import java.util.*

@Service
class TruckService(private val repository: TruckRepository) {

    fun get(uuid: UUID): Truck = repository.findById(uuid).orElseThrow()

    fun getAll(buyingOrder: BuyingOrder, page: Int, size: Int): List<Truck> =
        repository.findAllByBuyingOrderContaining(buyingOrder, PageRequest.of(page, size))

    fun assignTruck(): Truck = repository.findAll().random()
}
