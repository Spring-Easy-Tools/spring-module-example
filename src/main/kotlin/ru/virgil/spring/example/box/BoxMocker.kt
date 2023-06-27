package ru.virgil.spring.example.box

import net.datafaker.Faker
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.EntityMocker
import ru.virgil.spring.example.mock.MockAuthSuccessHandler
import ru.virgil.spring.example.truck.TruckMocker
import java.util.*

@Lazy
@Component
class BoxMocker(
    override val repository: BoxMockerRepository,
    override val authHandler: MockAuthSuccessHandler,
    private val truckMocker: TruckMocker,
    private val faker: Faker,
) : EntityMocker<Box> {

    override fun new(): Box {
        val truck = truckMocker.repository.findFirstByBoxesIsEmpty() ?: truckMocker.random()
        return Box(
            BoxType.values().random(),
            truck,
            faker.chuckNorris().fact(),
            Random().nextInt(500),
            Random().nextFloat(20f, 50f),
        ).also { it.createdBy = principal }
    }

    override fun random(): Box = repository.findAllByCreatedBy(principal).random()
}
