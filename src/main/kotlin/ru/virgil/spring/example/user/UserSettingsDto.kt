package ru.virgil.spring.example.user

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.time.ZonedDateTime
import java.util.*

data class UserSettingsDto(
    override var createdAt: ZonedDateTime?,
    override var updatedAt: ZonedDateTime?,
    override var uuid: UUID?,
    var name: String? = null,
) : IdentifiedDto, TimedDto
