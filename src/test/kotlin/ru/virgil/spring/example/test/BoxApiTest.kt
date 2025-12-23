package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.floats.shouldBeGreaterThanOrEqual
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import ru.virgil.spring.example.box.BoxDto
import ru.virgil.spring.example.box.BoxGenerator
import ru.virgil.spring.example.box.BoxType
import ru.virgil.spring.example.roles.police.WithMockedPoliceman
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.example.truck.TruckGenerator
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.asserting.AssertUtils
import ru.virgil.spring.tools.asserting.PartialMatcher
import ru.virgil.spring.tools.testing.MockMvcExtensions.jsonBody
import ru.virgil.spring.tools.testing.MockMvcExtensions.printResponse
import ru.virgil.spring.tools.testing.MockMvcExtensions.readResponse
import tools.jackson.databind.ObjectMapper

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class BoxApiTest @Autowired constructor(
    override val assertUtils: AssertUtils,
    val faker: Faker,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    private val boxGenerator: BoxGenerator,
    private val truckGenerator: TruckGenerator,
) : PartialMatcher {

    private val page = 0
    private val size = 10

    @Test
    fun getAll() {
        boxGenerator.generate(100).also { boxGenerator.repository.saveAll(it) }
        val boxDtoList: List<BoxDto> = mockMvc.get("/box?${RestValues.PAGE}=$page&${RestValues.SIZE}=$size") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.printResponse(objectMapper)
            .readResponse(objectMapper)
        boxDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val randomBox = boxGenerator.generate()
            .also { it.type = BoxType.USUAL }
            .also { boxGenerator.repository.save(it) }
        val boxDto: BoxDto = mockMvc.get("/box/${randomBox.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        boxDto.weight!! shouldBeGreaterThanOrEqual 10f
    }

    @Test
    fun createWithoutType() {
        val testDto = BoxDto(null, null, null, null, faker.appliance().brand(), 50000, 658f)
        mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun create() {
        truckGenerator.generate().also { truckGenerator.repository.save(it) }
        val testDto = BoxDto(type = BoxType.USUAL, description = "CREATED", price = 50000, weight = 658f)
        val createdDto: BoxDto = mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        createdDto shouldBePartialEquals testDto
        val serverDto: BoxDto = mockMvc.get("/box/${createdDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        serverDto shouldBeEqual createdDto
    }

    @Test
    fun edit() {
        val testDto = BoxDto(type = BoxType.USUAL, description = "EDITED", price = 78434, weight = 456f)
        val randomBox = boxGenerator.generate().also { boxGenerator.repository.save(it) }
        val changedDto: BoxDto = mockMvc.put("/box/${randomBox.uuid}") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        changedDto shouldBePartialEquals testDto
        val serverDto: BoxDto = mockMvc.get("/box/${changedDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        serverDto shouldBeEqual changedDto
    }

    @Test
    fun delete() {
        val box = boxGenerator.generate().also { boxGenerator.repository.save(it) }
        mockMvc.delete("/box/${box.uuid}") {
            with(testSecurityContext())
            with(csrf())
        }.andExpect {
            status { isOk() }
        }
        mockMvc.get("/box/${box.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun createWeaponByUsualUser() {
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-USUAL-USER", price = 50000, weight = 658f)
        mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockedPoliceman
    fun createWeaponByPoliceman() {
        truckGenerator.generate().also { truckGenerator.repository.save(it) }
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-POLICEMAN", price = 50000, weight = 658f)
        val createdDto: BoxDto = mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        createdDto shouldBePartialEquals testDto
        val serverDto: BoxDto = mockMvc.get("/box/${createdDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        createdDto shouldBeEqual serverDto
    }

    @Test
    fun getAllWeaponsByUsualUser() {
        mockMvc.get("/box/weapons?${RestValues.PAGE}=$page&${RestValues.SIZE}=$size") {
            with(testSecurityContext())
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockedPoliceman
    fun getAllWeaponsByPoliceman() {
        truckGenerator.generate().also { truckGenerator.repository.save(it) }
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-POLICEMAN", price = 50000, weight = 658f)
        val serverDto: BoxDto = mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            jsonBody(testDto, objectMapper)
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        serverDto shouldBePartialEquals testDto
        val weaponDtoList: List<BoxDto> = mockMvc.get("/box/weapons?${RestValues.PAGE}=$page&${RestValues.SIZE}=$size") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        weaponDtoList shouldContain serverDto
    }
}
