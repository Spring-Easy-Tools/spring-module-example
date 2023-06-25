package ru.virgil.spring.example.order

import ru.virgil.spring.example.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

class BuyingOrderDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var description: String?,
) : IdentifiedDto
