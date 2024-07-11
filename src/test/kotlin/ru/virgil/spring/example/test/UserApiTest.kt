package ru.virgil.spring.example.test

import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiTest @Autowired constructor(val fluent: Fluent) {

    @Test
    fun get() {
        val userSettingsDto: UserSettingsDto = fluent.request { get { "/user_settings" } }
        userSettingsDto.shouldNotBeNull()
    }
}
