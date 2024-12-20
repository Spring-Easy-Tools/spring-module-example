package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.example.roles.user.WithMockFirebaseUser
import ru.virgil.spring.tools.security.cors.CorsProperties
import ru.virgil.spring.tools.toolsBasePackage

@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
@WithMockFirebaseUser
class TestPropertiesAccess @Autowired constructor(
    @Value("\${spring.datasource.url}")
    val default: String,
    val corsProperties: CorsProperties,
    @Value("\${security.cors.origins}")
    val cors: List<String>,
) {

    @Test
    fun accessDefaultProperty() {
        println("Default property: $default")
        default.shouldNotBeEmpty()
    }

    @Test
    fun directAccessCustomProperty() {
        println("Custom property: ${corsProperties.origins}")
        corsProperties.origins shouldContain "http://localhost:4200"
    }

    @Test
    fun accessCustomProperty() {
        println("Custom property: $cors")
        cors shouldContain "http://localhost:4200"
    }
}
