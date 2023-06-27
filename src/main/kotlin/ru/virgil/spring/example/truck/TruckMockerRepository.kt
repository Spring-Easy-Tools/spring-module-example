package ru.virgil.spring.example.truck

import org.springframework.stereotype.Repository
import ru.virgil.spring.example.mock.MockerRepository
import java.util.*

@Repository
interface TruckMockerRepository : MockerRepository<Truck, UUID> {

    fun findFirstByBoxesIsEmpty(): Truck?
    fun findFirstByBuyingOrderIsEmpty(): Truck?
}
