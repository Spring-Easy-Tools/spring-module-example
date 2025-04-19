package ru.virgil.spring.example.truck

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime

import java.util.*

data class TruckDto(
    override var createdAt: ZonedDateTime? = null,
    override var updatedAt: ZonedDateTime? = null,
    override var uuid: UUID? = null,
    var boxesCount: Int? = null,
    var bestBoxUUID: UUID? = null,
) : IdentifiedDto, TimedDto
