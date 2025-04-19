package ru.virgil.spring.example.user

import ru.virgil.spring.example.system.dto.IdentifiedDto
import ru.virgil.spring.example.system.dto.TimedDto
import java.net.URI
import java.time.ZonedDateTime
import java.util.*

data class UserSettingsDto(
    override var createdAt: ZonedDateTime? = null,
    override var updatedAt: ZonedDateTime? = null,
    override var uuid: UUID? = null,
    var name: String? = null,
    var email: String? = null,
    var avatar: URI? = null,
) : IdentifiedDto, TimedDto
