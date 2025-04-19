package ru.virgil.spring.example.image


import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

data class PrivateImageFileDto(
    override var uuid: UUID? = null,
    override var createdAt: ZonedDateTime? = null,
    override var updatedAt: ZonedDateTime? = null,
    var name: String? = null,
) : IdentifiedDto, TimedDto
