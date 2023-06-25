package ru.virgil.spring.example.truck

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.MockerUtils
import java.util.*

@Lazy
@Component
class TruckMocker(
    private val truckRepository: TruckRepository,
) : MockerUtils {

    @Bean(new)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun new(): Truck = Truck()

    @Bean(random)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun random(): Truck = truckRepository.findAll().random()

    companion object {

        private const val name = "truck"
        const val new = "new-$name"
        const val random = "random-$name"
    }
}
