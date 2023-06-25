package ru.virgil.spring.example.order

import org.springframework.stereotype.Repository
import ru.virgil.spring.example.mock.ByOwner
import ru.virgil.spring.example.mock.MockerRepository
import java.util.*

@Repository
interface BuyingOrderMockerRepository : MockerRepository<BuyingOrder, UUID>, ByOwner<BuyingOrder>
