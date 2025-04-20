package ru.virgil.spring.example.test

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.example.roles.user.WithMockFirebaseUser
import ru.virgil.spring.example.user.UserSettingsDto
import ru.virgil.spring.example.user.UserSettingsService
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockFirebaseUser
class UserApiTest @Autowired constructor(
    val fluent: Fluent,
    private val userSettingsService: UserSettingsService,
) {

    @Test
    fun get() {
        if (userSettingsService.get() != null) userSettingsService.delete()
        val createdUserSettingsDto: UserSettingsDto = fluent.request { post { "/user_settings" } }
        val currentUserSettingsDto: UserSettingsDto = fluent.request { get { "/user_settings" } }
        createdUserSettingsDto shouldBeEqual currentUserSettingsDto
    }
}
