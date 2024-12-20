package ru.virgil.spring.example.truck

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime

import java.util.*

data class TruckDto(
    override var createdAt: ZonedDateTime?,
    override var updatedAt: ZonedDateTime?,
    override var uuid: UUID?,
    var boxesCount: Int?,
    var bestBoxUUID: UUID?,
) : IdentifiedDto, TimedDto
