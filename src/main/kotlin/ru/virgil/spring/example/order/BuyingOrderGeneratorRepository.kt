package ru.virgil.spring.example.order

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import java.util.*

@Repository
interface BuyingOrderGeneratorRepository : CrudRepository<BuyingOrder, UUID>, OwnedRepository<BuyingOrder>
