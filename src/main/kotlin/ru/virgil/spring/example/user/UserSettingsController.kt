package ru.virgil.spring.example.user

import org.springframework.web.bind.annotation.*
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orNotFound

@GlobalCors
@RestController
@RequestMapping("/user_settings")
class UserSettingsController(
    private val userSettingsService: UserSettingsService,
) : UserSettingsMapper {

    @GetMapping
    fun get(): UserSettingsDto {
        val currentUser = userSettingsService.get().orNotFound()
        return currentUser.toDto()
    }

    @PostMapping
    fun post(): UserSettingsDto {
        val createdUser = userSettingsService.create()
        return createdUser.toDto()
    }

    @PutMapping
    fun put(@RequestBody userSettingsDto: UserSettingsDto): UserSettingsDto {
        val editedUserSettings = userSettingsService.edit(userSettingsDto.toEntity())
        return editedUserSettings.toDto()
    }
}
