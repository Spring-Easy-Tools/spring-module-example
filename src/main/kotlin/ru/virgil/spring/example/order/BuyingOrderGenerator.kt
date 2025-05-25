package ru.virgil.spring.example.order

import net.datafaker.Faker
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.EasyRandomProvider

@Component
class BuyingOrderGenerator(
    private val easyRandom: EasyRandom,
    override val repository: BuyingOrderGeneratorRepository,
) : EasyRandomProvider.Generator<BuyingOrder> {

    override fun generate(): BuyingOrder {
        return easyRandom.nextObject(BuyingOrder::class.java)
    }

    override fun generate(count: Int): List<BuyingOrder> {
        return easyRandom.objects(BuyingOrder::class.java, count).toList()
    }

    companion object : EasyRandomProvider.Parameters {

        private val faker = Faker()

        override fun apply(parameters: EasyRandomParameters): EasyRandomParameters {
            return super.apply(parameters)
                .randomize(FieldPredicates.named(BuyingOrder::description.name)) { faker.backToTheFuture().quote() }
        }
    }
}
