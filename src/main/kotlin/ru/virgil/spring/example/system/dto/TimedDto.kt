package ru.virgil.spring.example.system.dto

import java.time.LocalDateTime

interface TimedDto {

    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
}
