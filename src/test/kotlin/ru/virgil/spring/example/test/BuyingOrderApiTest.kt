package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mikael.urlbuilder.UrlBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.virgil.spring.example.order.BuyingOrderDto
import ru.virgil.spring.example.order.BuyingOrderGenerator
import ru.virgil.spring.example.order.BuyingOrderMapper
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.truck.TruckDto
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext
import ru.virgil.spring.tools.testing.MockMvcExtensions.readResponse
import tools.jackson.databind.ObjectMapper

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class BuyingOrderApiTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val buyingOrderGenerator: BuyingOrderGenerator,
) : BuyingOrderMapper {

    private val page = 0
    private val pageSize = 10

    @Test
    fun getAll() {
        buyingOrderGenerator.generateAndSave(100)
        val buyingOrderDtoList: List<BuyingOrderDto> = mockMvc.get("/buying_order?page=$page&size=$pageSize") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        buyingOrderDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val buyingOrder = buyingOrderGenerator.generateAndSave()
        val randomBuyingOrderDto: BuyingOrderDto = mockMvc.get("/buying_order/${buyingOrder.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        randomBuyingOrderDto.description!!.shouldNotBeEmpty()
    }

    @Test
    fun getTruckByOrder() {
        val buyingOrders = buyingOrderGenerator.generateAndSave(100)
        val uri = UrlBuilder.fromString("/buying_order/${buyingOrders.random().uuid}/truck")
            .addParameter("page", page.toString())
            .addParameter("size", pageSize.toString())
            .toString()
        val truckDtoList: List<TruckDto> = mockMvc.get(uri) {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.readResponse(objectMapper)
        truckDtoList.shouldNotBeEmpty()
    }
}
