package ru.virgil.spring.example.user

import org.springframework.web.bind.annotation.*
import ru.virgil.spring.example.security.DefaultCors
import security.cors.DefaultCorsJava

@DefaultCorsJava
@RestController
@RequestMapping("/user_settings")
class UserSettingsController(
    private val userSettingsService: UserSettingsService,
) : UserSettingsMapper {

    @GetMapping
    fun get(): UserSettingsDto {
        val currentUser = userSettingsService.get()
        return currentUser.toDto()
    }

    // TODO: Покрыть тестом
    @PutMapping
    fun put(@RequestBody userSettingsDto: UserSettingsDto): UserSettingsDto {
        val editedUserSettings = userSettingsService.edit(userSettingsDto.toEntity())
        return editedUserSettings.toDto()
    }
}
