package ru.virgil.spring.example.truck

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.EntityMocker
import ru.virgil.spring.example.mock.MockAuthSuccessHandler

@Lazy
@Component
class TruckMocker(
    override val repository: TruckMockerRepository,
    override val authHandler: MockAuthSuccessHandler,
) : EntityMocker<Truck> {

    override fun new(): Truck = Truck()

    override fun random(): Truck = repository.findAll().toList().random()
}
