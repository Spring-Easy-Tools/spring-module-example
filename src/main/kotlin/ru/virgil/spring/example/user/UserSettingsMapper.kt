package ru.virgil.spring.example.user

interface UserSettingsMapper {

    fun UserSettingsDto.toEntity() = UserSettings(
        name = name,
        email = email,
        avatar = avatar
    )

    fun UserSettings.toDto() = UserSettingsDto(
        createdAt = createdAt,
        updatedAt = updatedAt,
        uuid = uuid,
        name = name,
        email = email,
        avatar = avatar
    )

    infix fun UserSettings.merge(userSettings: UserSettings): UserSettings {
        name = userSettings.name ?: name
        email = userSettings.email ?: email
        avatar = userSettings.avatar ?: avatar
        return this
    }
}
