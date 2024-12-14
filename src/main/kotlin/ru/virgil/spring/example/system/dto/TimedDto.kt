package ru.virgil.spring.example.system.dto

import java.time.ZonedDateTime

interface TimedDto {

    var createdAt: ZonedDateTime?
    var updatedAt: ZonedDateTime?
}
