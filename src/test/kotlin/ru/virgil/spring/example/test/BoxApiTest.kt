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
import org.springframework.test.web.servlet.*
import ru.virgil.spring.example.box.BoxDto
import ru.virgil.spring.example.box.BoxGenerator
import ru.virgil.spring.example.box.BoxType
import ru.virgil.spring.example.roles.police.WithMockedPoliceman
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.truck.TruckGenerator
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.asserting.AssertUtils.Companion.shouldContainAllFieldsFrom
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.fromJson
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.jsonBody
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.printRequest
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.printResponse
import tools.jackson.databind.ObjectMapper

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class BoxApiTest @Autowired constructor(
    val faker: Faker,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    private val boxGenerator: BoxGenerator,
    private val truckGenerator: TruckGenerator,
) {

    private val page = 0
    private val size = 10

    @Test
    fun getAll() {
        boxGenerator.generate(100).also { boxGenerator.repository.saveAll(it) }
        val boxDtoList: List<BoxDto> = mockMvc.get("/box?page=$page&size=$size") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andDo {
            printResponse()
        }.andReturn().fromJson()
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
        }.andReturn().fromJson()
        boxDto.weight!! shouldBeGreaterThanOrEqual 10f
    }

    @Test
    fun createWithoutType() {
        val testDto = BoxDto(null, null, null, null, faker.appliance().brand(), 50000, 658f)
        mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            with(jsonBody(testDto))
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            printRequest()
            printResponse()
        }
    }

    @Test
    fun create() {
        truckGenerator.generate().also { truckGenerator.repository.save(it) }
        val testDto = BoxDto(type = BoxType.USUAL, description = "CREATED", price = 50000, weight = 658f)
        val createdDto: BoxDto = mockMvc.post("/box") {
            with(testSecurityContext())
            with(csrf())
            with(jsonBody(testDto))
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        createdDto shouldContainAllFieldsFrom testDto
        val serverDto: BoxDto = mockMvc.get("/box/${createdDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        serverDto shouldBeEqual createdDto
    }

    @Test
    fun edit() {
        val testDto = BoxDto(type = BoxType.USUAL, description = "EDITED", price = 78434, weight = 456f)
        val randomBox = boxGenerator.generate().also { boxGenerator.repository.save(it) }
        val changedDto: BoxDto = mockMvc.put("/box/${randomBox.uuid}") {
            with(testSecurityContext())
            with(csrf())
            with(jsonBody(testDto))
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        changedDto shouldContainAllFieldsFrom testDto
        val serverDto: BoxDto = mockMvc.get("/box/${changedDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
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
            with(jsonBody(testDto))
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
            with(jsonBody(testDto))
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        createdDto shouldContainAllFieldsFrom testDto
        val serverDto: BoxDto = mockMvc.get("/box/${createdDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        createdDto shouldBeEqual serverDto
    }

    @Test
    fun getAllWeaponsByUsualUser() {
        mockMvc.get("/box/weapons?page=$page&size=$size") {
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
            with(jsonBody(testDto))
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        serverDto shouldContainAllFieldsFrom testDto
        val weaponDtoList: List<BoxDto> = mockMvc.get("/box/weapons?page=$page&size=$size") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        weaponDtoList shouldContain serverDto
    }
}
