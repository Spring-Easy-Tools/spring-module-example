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
import ru.virgil.spring.example.stats.StatsDto
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.toolsBasePackage

@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsApiTest @Autowired constructor(val fluent: Fluent) {

    @Test
    fun getAll() {
        val statsDto: StatsDto = fluent.request { get { "/stats/all" } }
        statsDto.shouldNotBeNull()
    }

    @Test
    fun getMy() {
        val statsDto: StatsDto = fluent.request { get { "/stats/my" } }
        statsDto.shouldNotBeNull()
    }
}
