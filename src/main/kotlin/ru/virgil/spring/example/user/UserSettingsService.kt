package ru.virgil.spring.example.user

import org.jeasy.random.EasyRandom
import org.springframework.stereotype.Service
import ru.virgil.spring.example.security.SecurityUserService

@Service
class UserSettingsService(
    private val repository: UserSettingsRepository,
    private val securityUserService: SecurityUserService,
    private val easyRandom: EasyRandom,
) : UserSettingsMapper {

    fun get(): UserSettings {
        val securityUser = securityUserService.principal
        return repository.findByCreatedBy(securityUser).orElseThrow()!!
    }

    fun edit(userSettings: UserSettings): UserSettings {
        val currentUserSettings = get()
        val editedUserSettings = currentUserSettings merge userSettings
        return repository.save(editedUserSettings)
    }

    fun create(): UserSettings {
        val userSettings = easyRandom.nextObject(UserSettings::class.java).also { repository.save(it) }
        return repository.save(userSettings)
    }

    // @EventListener
    // fun onSuccess(success: AuthenticationSuccessEvent?) {
    //
    //     principal = success!!.authentication.principal as SecurityUser
    //     mocker.start()
    // }

    companion object {

        private const val name = "user-details"
        const val current = "current-$name"
        const val mocking = "mocking-$name"
    }
}
