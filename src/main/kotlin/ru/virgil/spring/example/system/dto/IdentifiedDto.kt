package ru.virgil.spring.example.system.dto

import java.util.*

interface IdentifiedDto : TimedDto {

    var uuid: UUID?
}
