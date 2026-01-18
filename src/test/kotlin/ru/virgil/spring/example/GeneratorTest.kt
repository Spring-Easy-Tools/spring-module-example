package ru.virgil.spring.example

import tools.jackson.databind.ObjectMapper
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import ru.virgil.spring.example.box.BoxGenerator
import ru.virgil.spring.example.order.BuyingOrderGenerator
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.system.InstancioProvider
import ru.virgil.spring.example.truck.TruckGenerator
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.util.logging.Logger

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class GeneratorTest @Autowired constructor(
    private val buyingOrderGenerator: BuyingOrderGenerator,
    private val boxGenerator: BoxGenerator,
    private val truckGenerator: TruckGenerator,
    private val objectMapper: ObjectMapper,
) {

    private val logger = Logger.inject(this::class.java)

    private fun <Data : Any> testGenerator(generator: InstancioProvider.Generator<Data>) {
        // Для полноценной работы все сущности должны быть CASCADE
        val generatedList = generator.generate(3).also { generator.repository.saveAll(it) }
        logger.info { objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(generatedList) }
        generatedList.shouldNotBeEmpty()
    }

    @Test
    fun buyingOrderGeneration() {
        testGenerator(buyingOrderGenerator)
    }

    @Test
    fun boxGeneration() {
        testGenerator(boxGenerator)
    }

    @Test
    fun truckGenerator() {
        testGenerator(truckGenerator)
    }
}
