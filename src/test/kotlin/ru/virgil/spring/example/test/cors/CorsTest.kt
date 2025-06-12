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

    /**
     * Проверяет, что для каждого разрешённого origin CORS возвращает правильный заголовок Access-Control-Allow-Origin.
     */
    @Test
    fun testAllowedOrigins() {
        corsProperties.origins.forEach { origin ->
            mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, origin) }
                .andExpect {
                    status { isOk() }
                    header { string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin) }
                }
        }
    }

    /**
     * Проверяет, что для неразрешённого origin возвращается статус Forbidden.
     */
    @Test
    fun testNotAllowedOrigin() {
        val wrongOrigin = faker.domain().validDomain(faker.company().name().toKebabCase())
        mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, wrongOrigin) }
            .andExpect { status { isForbidden() } }
            .andDo { print() }
    }

    /**
     * Проверяет, что все заголовки из exposedHeaders действительно присутствуют в ответе CORS.
     */
    @Test
    fun testExposedHeaders() {
        corsProperties.origins.forEach { origin ->
            val result = mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, origin) }
                .andReturn()
            val exposedHeaders = result.response.getHeaderValue(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
            corsProperties.exposedHeaders.forEach { header ->
                assert(exposedHeaders?.toString()?.contains(header) == true) {
                    "Header $header should be exposed in CORS response, but was: $exposedHeaders"
                }
            }
        }
    }

    /**
     * Проверяет, что allowCredentials корректно отражается в заголовке Access-Control-Allow-Credentials.
     */
    @Test
    fun testAllowCredentials() {
        corsProperties.origins.forEach { origin ->
            val result = mockMvc.get("/ping") { header(HttpHeaders.ORIGIN, origin) }
                .andReturn()
            val allowCredentials = result.response.getHeaderValue(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
            if (corsProperties.allowCredentials) {
                assert(allowCredentials == "true") { "CORS should allow credentials, but got: $allowCredentials" }
            } else {
                assert(allowCredentials == null || allowCredentials == "false") { "CORS should not allow credentials, but got: $allowCredentials" }
            }
        }
    }
}
