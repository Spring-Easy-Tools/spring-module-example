package ru.virgil.spring.example.truck

import ru.virgil.spring.example.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

data class TruckDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var boxesCount: Int?,
    var bestBoxUUID: UUID,
) : IdentifiedDto
