package ru.virgil.spring.example.test

import io.kotest.equals.Equality.Companion.default
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.security.cors.CorsProperties

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
class TestPropertiesAccess @Autowired constructor(
    val corsProperties: CorsProperties,
) {

    @Value("\${spring.datasource.url}")
    lateinit var default: String

    @Value("\${security.cors.origins}")
    lateinit var cors: List<String>

    @Test
    fun accessDefaultProperty() {
        println("Default property: $default")
        default.shouldNotBeEmpty()
    }

    @Test
    fun directAccessCustomProperty() {
        println("Custom property: ${corsProperties.origins}")
        corsProperties.origins shouldContain "http://localhost:8080"
    }

    @Test
    fun accessCustomProperty() {
        println("Custom property: $cors")
        cors shouldContain "http://localhost:8080"
    }
}
