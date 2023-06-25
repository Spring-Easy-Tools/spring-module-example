package ru.virgil.spring.example.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import java.util.*

@Repository
interface UserSettingsRepository : JpaRepository<UserSettings, UUID>, OwnedRepository<UserSettings> {

    fun findByCreatedBy(createdBy: UserDetails): Optional<UserSettings>
}
