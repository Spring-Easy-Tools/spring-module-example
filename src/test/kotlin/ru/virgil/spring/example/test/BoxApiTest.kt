package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.floats.shouldBeGreaterThanOrEqual
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.virgil.spring.example.box.BoxDto
import ru.virgil.spring.example.box.BoxMocker
import ru.virgil.spring.example.box.BoxService
import ru.virgil.spring.example.box.BoxType
import ru.virgil.spring.example.roles.police.WithMockFirebasePoliceman
import ru.virgil.spring.example.roles.user.WithMockFirebaseUser
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.tools.asserting.AssertUtils
import ru.virgil.spring.tools.asserting.PartialMatcher
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.toolsBasePackage

@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoxApiTest @Autowired constructor(
    override val assertUtils: AssertUtils,
    val faker: Faker,
    val fluent: Fluent,
    val boxMocker: BoxMocker,
    val boxService: BoxService,
) : PartialMatcher {

    private val page = 0
    private val size = 10

    @Test
    fun getAll() {
        val boxDtoList: MutableList<BoxDto> = fluent.request {
            get { "/box?${RestValues.page}=$page&${RestValues.size}=$size" }
        }
        boxDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val randomBox = boxService.getAll(0, 10).last { it.type == BoxType.USUAL }
        val boxDto: BoxDto = fluent.request { get { "/box/${randomBox.uuid}" } }
        boxDto.weight!! shouldBeGreaterThanOrEqual 10f
    }

    @Test
    fun createWithoutType() {
        val testDto = BoxDto(null, null, null, null, faker.appliance().brand(), 50000, 658f)
        fluent.request<Any> {
            post { "/box" }
            send { testDto }
            expect { status().isBadRequest }
        }
    }

    @Test
    fun create() {
        val testDto = BoxDto(type = BoxType.USUAL, description = "CREATED", price = 50000, weight = 658f)
        val createdDto: BoxDto = fluent.request {
            post { "/box" }
            send { testDto }
        }
        createdDto shouldBePartialEquals testDto
        val serverDto: BoxDto = fluent.request { get { "/box/${createdDto.uuid}" } }
        serverDto shouldBeEqual createdDto
    }

    @Test
    fun edit() {
        val testDto = BoxDto(type = BoxType.USUAL, description = "EDITED", price = 78434, weight = 456f)
        val randomBox = boxMocker.random()
        val changedDto: BoxDto = fluent.request {
            put { "/box/${randomBox.uuid}" }
            send { testDto }
        }
        changedDto shouldBePartialEquals testDto
        val serverDto: BoxDto = fluent.request { get { "/box/${changedDto.uuid}" } }
        serverDto shouldBeEqual changedDto
    }

    @Test
    fun delete() {
        val box = boxMocker.random()
        fluent.request<Any> { delete { "/box/${box.uuid}" } }
        fluent.request<Any> {
            get { "/box/${box.uuid}" }
            expect { status().isNotFound }
        }
    }

    @Test
    fun createWeaponByUsualUser() {
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-USUAL-USER", price = 50000, weight = 658f)
        fluent.request<Any> {
            post { "/box" }
            send { testDto }
            expect { status().isForbidden }
        }
    }

    @Test
    @WithMockFirebasePoliceman
    fun createWeaponByPoliceman() {
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-POLICEMAN", price = 50000, weight = 658f)
        val createdDto: BoxDto = fluent.request {
            post { "/box" }
            send { testDto }
        }
        createdDto shouldBePartialEquals testDto
        val serverDto: BoxDto = fluent.request { get { "/box/${createdDto.uuid}" } }
        createdDto shouldBeEqual serverDto
    }

    @Test
    fun getAllWeaponsByUsualUser() {
        // TODO: Исправить работу @PostAuthorize
        // fluent.request<Any> {
        //     get { "/box/weapons?${RestValues.page}=$page&${RestValues.size}=$size" }
        //     expect { status().isForbidden }
        // }
    }

    @WithMockFirebasePoliceman
    @Test
    fun getAllWeaponsByPoliceman() {
        val testDto = BoxDto(type = BoxType.WEAPON, description = "CREATED-BY-POLICEMAN", price = 50000, weight = 658f)
        val serverDto: BoxDto = fluent.request {
            post { "/box" }
            send { testDto }
        }
        serverDto shouldBePartialEquals testDto
        val weaponDtoList: List<BoxDto> = fluent.request {
            get { "/box/weapons?${RestValues.page}=$page&${RestValues.size}=$size" }
        }
        weaponDtoList shouldContain serverDto
    }
}
