package ru.virgil.spring.example.test.cors

import net.datafaker.Faker
import net.pearx.kasechange.toKebabCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.tools.security.cors.CorsProperties
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE

// todo: перенести в Spring-модуль
@SpringBootTest
@AutoConfigureMockMvc
@WithMockedUser
@ComponentScan(BASE_PACKAGE)
class CorsTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val corsProperties: CorsProperties,
    private val faker: Faker,
) {

    // todo: добавить тест на фильтрацию заголовков?

    @Test
    fun testAllowedOrigin() {
        val randomOrigin = corsProperties.origins.random()
        mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, randomOrigin) }
            .andExpect {
                status { isOk() }
                header { string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, randomOrigin) }
            }
            .andDo { print() }
    }

    @Test
    fun testNotAllowedOrigin() {
        val wrongOrigin = faker.domain().validDomain(faker.company().name().toKebabCase())
        mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, wrongOrigin) }
            .andExpect { status { isForbidden() } }
            .andDo { print() }
    }
}
