package ru.virgil.spring.example.user

import ru.virgil.spring.example.system.dto.IdentifiedDto
import java.time.LocalDateTime
import java.util.*

// TODO: data class
class UserSettingsDto(
    override var createdAt: LocalDateTime?,
    override var updatedAt: LocalDateTime?,
    override var uuid: UUID?,
    var name: String? = null,
) : IdentifiedDto
