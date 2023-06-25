package ru.virgil.spring.example.box

import ru.virgil.spring.example.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

data class BoxDto(
    override var uuid: UUID? = null,
    override var createdAt: LocalDateTime? = null,
    override var updatedAt: LocalDateTime? = null,
    var type: BoxType? = BoxType.USUAL,
    var description: String?,
    var price: Int? = 0,
    var weight: Float? = 0f,
) : IdentifiedDto
