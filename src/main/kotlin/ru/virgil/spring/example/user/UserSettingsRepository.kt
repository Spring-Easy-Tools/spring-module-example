package ru.virgil.spring.example.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSettingsRepository : JpaRepository<UserSettings, UUID> {

    fun findByCreatedBy(createdBy: String): UserSettings?
}
