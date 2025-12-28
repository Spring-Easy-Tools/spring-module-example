package ru.virgil.spring.example.test

import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.stats.StatsDto
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.fromJson
import tools.jackson.databind.ObjectMapper

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class StatsApiTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
) {

    @Test
    fun getAll() {
        val statsDto: StatsDto = mockMvc.get("/stats/all") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        statsDto.shouldNotBeNull()
    }

    @Test
    fun getMy() {
        val statsDto: StatsDto = mockMvc.get("/stats/my") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        statsDto.shouldNotBeNull()
    }
}
