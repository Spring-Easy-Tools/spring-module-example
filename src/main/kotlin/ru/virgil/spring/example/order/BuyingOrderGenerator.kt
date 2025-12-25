package ru.virgil.spring.example.order

import net.datafaker.Faker
import org.instancio.Instancio
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.InstancioProvider
import ru.virgil.spring.example.system.KSelect
import java.util.function.Supplier

@Component
class BuyingOrderGenerator(
    private val instancioProvider: InstancioProvider,
    override val repository: BuyingOrderGeneratorRepository,
) : InstancioProvider.Generator<BuyingOrder> {

    private val faker = Faker()

    override fun generate(): BuyingOrder {
        return Instancio.of(instancioProvider.createModel(BuyingOrder::class.java))
            .supply(KSelect.field(BuyingOrder::description), Supplier { faker.backToTheFuture().quote() })
            .create()
    }

    override fun generate(count: Int): List<BuyingOrder> {
        return (1..count).map { generate() }
    }
}
