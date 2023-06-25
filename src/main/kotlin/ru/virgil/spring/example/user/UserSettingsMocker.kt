package ru.virgil.spring.example.user

import net.datafaker.Faker
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.EntityMocker
import ru.virgil.spring.example.mock.MockAuthSuccessHandler

@Lazy
@Component
class UserSettingsMocker(
    override val repository: UserSettingsMockerRepository,
    override val authHandler: MockAuthSuccessHandler,
    val faker: Faker,
) : EntityMocker<UserSettings> {

    override fun new(): UserSettings = UserSettings(faker.name().fullName()).also { it.createdBy = principal }

    override fun random(): UserSettings = repository.findAllByCreatedBy(principal).random()
}
