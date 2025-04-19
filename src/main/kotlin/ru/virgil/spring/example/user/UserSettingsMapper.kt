package ru.virgil.spring.example.user

interface UserSettingsMapper {

    fun UserSettingsDto.toEntity(): UserSettings = UserSettings(name, email, avatar)

    fun UserSettings.toDto(): UserSettingsDto = UserSettingsDto(createdAt, updatedAt, uuid, name, email, avatar)

    infix fun UserSettings.merge(userSettings: UserSettings): UserSettings {
        name = userSettings.name ?: name
        email = userSettings.email ?: email
        avatar = userSettings.avatar ?: avatar
        return this
    }
}
