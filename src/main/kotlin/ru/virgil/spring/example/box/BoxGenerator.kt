package ru.virgil.spring.example.box

import net.datafaker.Faker
import org.instancio.Instancio
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.InstancioProvider
import ru.virgil.spring.example.system.KSelect
import java.util.function.Supplier
import kotlin.random.Random

@Component
class BoxGenerator(
    private val instancioProvider: InstancioProvider,
    override val repository: BoxGeneratorRepository,
) : InstancioProvider.Generator<Box> {

    private val faker = Faker()

    override fun generate(): Box = Instancio.of(instancioProvider.createModel(Box::class.java))
        .supply(KSelect.field(Box::description), Supplier { faker.science().element() })
        .supply(KSelect.field(Box::weight), Supplier { Random.nextLong(10, 10000).toFloat() })
        .create()

    override fun generate(count: Int): List<Box> = (1..count).map { generate() }
}
