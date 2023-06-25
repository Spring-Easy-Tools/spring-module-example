package ru.virgil.spring.example.order

import net.datafaker.Faker
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.MockAuthSuccessHandler
import ru.virgil.spring.example.mock.MockerUtils
import ru.virgil.spring.example.truck.TruckRepository

@Lazy
@Component
class BuyingOrderMocker(
    private val mockAuthSuccessHandler: MockAuthSuccessHandler,
    private val faker: Faker,
    private val buyingOrderRepository: BuyingOrderRepository,
    private val truckRepository: TruckRepository,
) : MockerUtils {

    @Lazy
    @Bean(new)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun new(): BuyingOrder {
        val truck = truckRepository.findPriorityOrGetRandom { it.buyingOrder.isEmpty() }
        return BuyingOrder(truck, faker.ancient().hero())
            .also { it.createdBy = mockAuthSuccessHandler.principal }
    }

    @Lazy
    @Bean(random)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun random(): BuyingOrder = buyingOrderRepository.findAllByCreatedBy(mockAuthSuccessHandler.principal).random()

    companion object {

        private const val name = "buying-order"
        const val new = "new-$name"
        const val random = "random-$name"
    }
}
