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
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.toolsBasePackage

@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
@WithMockFirebaseUser
class UserApiTest @Autowired constructor(
    val fluent: Fluent,
) {

    @Test
    fun get() {
        val createdUserSettingsDto: UserSettingsDto = fluent.request { post { "/user_settings" } }
        val currentUserSettingsDto: UserSettingsDto = fluent.request { get { "/user_settings" } }
        createdUserSettingsDto shouldBeEqual currentUserSettingsDto
    }
}
