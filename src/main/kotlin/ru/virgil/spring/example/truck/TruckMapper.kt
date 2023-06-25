package ru.virgil.spring.example.truck

interface TruckMapper {

    fun Truck.toDto() = TruckDto(createdAt, updatedAt, uuid, boxes.size)
}
