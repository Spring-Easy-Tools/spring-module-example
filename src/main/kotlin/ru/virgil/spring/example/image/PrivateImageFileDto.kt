package ru.virgil.spring.example.image


import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

data class PrivateImageFileDto(
    override var uuid: UUID?,
    override var createdAt: ZonedDateTime?,
    override var updatedAt: ZonedDateTime?,
    var name: String?,
) : IdentifiedDto, TimedDto
