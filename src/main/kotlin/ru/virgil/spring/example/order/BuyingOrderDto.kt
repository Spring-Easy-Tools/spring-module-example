package ru.virgil.spring.example.order

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

class BuyingOrderDto(
    override var createdAt: ZonedDateTime?,
    override var updatedAt: ZonedDateTime?,
    override var uuid: UUID?,
    var description: String?,
) : IdentifiedDto, TimedDto
