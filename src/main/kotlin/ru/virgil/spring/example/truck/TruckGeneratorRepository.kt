package ru.virgil.spring.example.truck

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TruckGeneratorRepository : CrudRepository<Truck, UUID> {

    fun findFirstByBoxesIsEmpty(): Truck?
    fun findFirstByBuyingOrderIsEmpty(): Truck?
}
