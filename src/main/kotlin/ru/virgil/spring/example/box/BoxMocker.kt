package ru.virgil.spring.example.box

import net.datafaker.Faker
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.MockAuthSuccessHandler
import ru.virgil.spring.example.mock.MockerUtils
import ru.virgil.spring.example.truck.TruckRepository
import java.util.*

@Lazy
@Component
class BoxMocker(
    private val mockAuthSuccessHandler: MockAuthSuccessHandler,
    private val truckRepository: TruckRepository,
    private val faker: Faker,
    private val boxRepository: BoxRepository,
) : MockerUtils {

    @Lazy
    @Bean(new)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun new(): Box {
        val truck = truckRepository.findPriorityOrGetRandom { it.boxes.isEmpty() }
        return Box(
            BoxType.values().random(),
            truck,
            faker.chuckNorris().fact(),
            Random().nextInt(500),
            Random().nextFloat(20f, 50f),
        ).also { it.createdBy = mockAuthSuccessHandler.principal }
    }

    @Lazy
    @Bean(random)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun random(): Box = boxRepository.findAllByCreatedBy(mockAuthSuccessHandler.principal).random()

    companion object {

        private const val name = "box"
        const val new = "new-$name"
        const val random = "random-$name"
    }
}
