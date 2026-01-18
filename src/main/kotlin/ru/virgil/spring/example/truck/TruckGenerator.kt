package ru.virgil.spring.example.truck

import org.instancio.Instancio
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.InstancioProvider

@Component
class TruckGenerator(
    private val instancioProvider: InstancioProvider,
    override val repository: TruckGeneratorRepository,
) : InstancioProvider.Generator<Truck> {

    override fun generate(): Truck = Instancio.of(instancioProvider.createModel(Truck::class.java)).create()

    override fun generate(count: Int): List<Truck> = (1..count).map { generate() }
}
