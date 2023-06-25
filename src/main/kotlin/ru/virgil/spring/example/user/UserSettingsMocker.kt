package ru.virgil.spring.example.user

import net.datafaker.Faker
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.virgil.spring.example.mock.MockAuthSuccessHandler
import ru.virgil.spring.example.mock.MockerUtils
import java.util.*

@Component
class UserSettingsMocker(
    val mockAuthSuccessHandler: MockAuthSuccessHandler,
    val repository: UserSettingsRepository,
    val faker: Faker,
) : MockerUtils {

    @Lazy
    @Bean(new)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun new(): UserSettings {
        return UserSettings(faker.name().fullName())
            .also { it.createdBy = mockAuthSuccessHandler.principal }
    }

    @Lazy
    @Bean(random)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun random(): UserSettings = repository.findAllByCreatedBy(mockAuthSuccessHandler.principal).random()

    companion object {

        private const val name = "user-settings"
        const val new = "new-$name"
        const val random = "random-$name"
    }
}
