package ru.virgil.spring.example.truck

import ru.virgil.spring.example.box.BoxService

interface TruckMapper {

    val boxService: BoxService

    fun Truck.toDto(): TruckDto {
        val bestBox = boxService.findBestBoxByTruck(this)
        return TruckDto(createdAt, updatedAt, uuid, boxes.size, bestBox)
    }
}
