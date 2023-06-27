package ru.virgil.spring.example.order

import net.datafaker.Faker
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.EntityMocker
import ru.virgil.spring.example.mock.MockAuthSuccessHandler
import ru.virgil.spring.example.truck.TruckMocker

@Lazy
@Component
class BuyingOrderMocker(
    override val authHandler: MockAuthSuccessHandler,
    override val repository: BuyingOrderMockerRepository,
    private val truckMocker: TruckMocker,
    private val faker: Faker,
) : EntityMocker<BuyingOrder> {

    override fun new(): BuyingOrder {
        val truck = truckMocker.repository.findFirstByBuyingOrderIsEmpty() ?: truckMocker.random()
        return BuyingOrder(truck, faker.ancient().hero())
            .also { it.createdBy = principal }
    }

    override fun random(): BuyingOrder = repository.findAllByCreatedBy(principal).random()
}
