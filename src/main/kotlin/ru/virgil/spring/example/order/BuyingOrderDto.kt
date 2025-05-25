package ru.virgil.spring.example.order

import ru.virgil.spring.tools.dto.IdentifiedDto
import ru.virgil.spring.tools.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

class BuyingOrderDto(
    override var createdAt: ZonedDateTime? = null,
    override var updatedAt: ZonedDateTime? = null,
    override var uuid: UUID? = null,
    var description: String? = null,
) : IdentifiedDto, TimedDto
