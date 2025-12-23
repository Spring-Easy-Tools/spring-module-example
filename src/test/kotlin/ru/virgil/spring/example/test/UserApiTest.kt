package ru.virgil.spring.example.test

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.user.UserSettingsDto
import ru.virgil.spring.example.user.UserSettingsService
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext
import ru.virgil.spring.tools.testing.MockMvcExtensions.readResponse
import tools.jackson.databind.ObjectMapper

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class UserApiTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    private val userSettingsService: UserSettingsService,
) {

    @Test
    fun get() {
        if (userSettingsService.get() != null) userSettingsService.delete()
        val createdUserSettingsDto: UserSettingsDto = mockMvc.post("/user_settings") {
            with(testSecurityContext())
            with(csrf())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        val currentUserSettingsDto: UserSettingsDto = mockMvc.get("/user_settings") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        createdUserSettingsDto shouldBeEqual currentUserSettingsDto
    }
}
