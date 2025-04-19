package ru.virgil.spring.example.box

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

data class BoxDto(
    override var uuid: UUID? = null,
    override var createdAt: ZonedDateTime? = null,
    override var updatedAt: ZonedDateTime? = null,
    var type: BoxType? = BoxType.USUAL,
    var description: String? = null,
    var price: Int? = 0,
    var weight: Float? = 0f,
) : IdentifiedDto, TimedDto
