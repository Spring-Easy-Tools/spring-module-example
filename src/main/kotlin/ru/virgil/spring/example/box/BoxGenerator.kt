package ru.virgil.spring.example.box

import net.datafaker.Faker
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.EasyRandomProvider
import kotlin.random.Random

@Component
class BoxGenerator(
    private val easyRandom: EasyRandom,
    override val repository: BoxGeneratorRepository,
) : EasyRandomProvider.Generator<Box> {

    override fun generate() = easyRandom.nextObject(Box::class.java)

    override fun generate(count: Int) = easyRandom.objects(Box::class.java, count).toList()

    companion object : EasyRandomProvider.Parameters {

        private val faker = Faker()

        override fun apply(parameters: EasyRandomParameters): EasyRandomParameters {
            return super.apply(parameters)
                .randomize(FieldPredicates.named(Box::description.name)) { faker.science().element() }
                .randomize(FieldPredicates.named(Box::weight.name)) { Random.nextLong(10, 10000).toFloat() }
        }
    }
}
