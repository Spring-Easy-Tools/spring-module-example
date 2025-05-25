package ru.virgil.spring.example.box

import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.util.Http.orBadRequest

interface BoxMapper {

    fun BoxDto.toEntity(truck: Truck) = Box(
        type = type.orBadRequest(),
        truck = truck,
        description = description.orBadRequest(),
        price = price.orBadRequest(),
        weight = weight.orBadRequest()
    )

    fun Box.toDto() = BoxDto(
        uuid = uuid,
        createdAt = createdAt,
        updatedAt = updatedAt,
        type = type,
        description = description,
        price = price,
        weight = weight
    )

    infix fun Box.merge(boxDto: BoxDto): Box {
        price = boxDto.price ?: price
        type = boxDto.type ?: type
        description = boxDto.description ?: description
        weight = boxDto.weight ?: weight
        return this
    }
}
