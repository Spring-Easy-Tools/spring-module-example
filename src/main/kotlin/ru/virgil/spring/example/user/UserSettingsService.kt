package ru.virgil.spring.example.user

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.Security
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import ru.virgil.spring.tools.util.Http.orNotFound
import ru.virgil.spring.tools.util.Http.thenConflict
import java.net.URI

@Service
class UserSettingsService(
    private val repository: UserSettingsRepository,
) : UserSettingsMapper {

    fun get(): UserSettings? = repository.findByCreatedBy(getSimpleCreator())

    fun edit(userSettings: UserSettings): UserSettings {
        val currentUserSettings = get().orNotFound()
        val editedUserSettings = currentUserSettings merge userSettings
        return repository.save(editedUserSettings)
    }

    fun create(): UserSettings {
        get().thenConflict()
        val authentication = Security.getAuthentication()
        val userSettings = when (authentication) {
            is OAuth2AuthenticationToken -> formOauthUserSettings(authentication)
            else -> formFallbackUserSettings(authentication)
        }
        return repository.save(userSettings)
    }

    private fun formFallbackUserSettings(authentication: Authentication) = UserSettings(authentication.name)

    private fun formOauthUserSettings(authentication: OAuth2AuthenticationToken): UserSettings {
        val principal = authentication.principal as OidcUser
        val userSettings = UserSettings(
            principal.fullName,
            principal.email,
            URI.create(principal.picture)
        )
        return userSettings
    }

    fun delete() {
        repository.delete(get().orNotFound())
    }
}
