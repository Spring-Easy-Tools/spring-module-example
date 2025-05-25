package ru.virgil.spring.example.truck

import org.jeasy.random.EasyRandom
import org.springframework.stereotype.Component
import ru.virgil.spring.example.system.EasyRandomProvider

@Component
class TruckGenerator(
    private val easyRandom: EasyRandom,
    override val repository: TruckGeneratorRepository,
) : EasyRandomProvider.Generator<Truck> {

    override fun generate() = easyRandom.nextObject(Truck::class.java)

    override fun generate(count: Int) = easyRandom.objects(Truck::class.java, count).toList()

    companion object : EasyRandomProvider.Parameters
}
