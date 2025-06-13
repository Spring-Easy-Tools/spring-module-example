package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mikael.urlbuilder.UrlBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.example.order.BuyingOrderDto
import ru.virgil.spring.example.order.BuyingOrderGenerator
import ru.virgil.spring.example.order.BuyingOrderMapper
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.example.truck.TruckDto
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@WithMockedUser
class BuyingOrderApiTest @Autowired constructor(
    private val fluent: Fluent,
    private val buyingOrderGenerator: BuyingOrderGenerator,
) : BuyingOrderMapper {

    private val page = 0
    private val pageSize = 10

    @Test
    fun getAll() {
        buyingOrderGenerator.generateAndSave(100)
        val buyingOrderDtoList: List<BuyingOrderDto> = fluent.request {
            get { "/buying_order?${RestValues.PAGE}=$page&${RestValues.SIZE}=$pageSize" }
        }
        buyingOrderDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val buyingOrder = buyingOrderGenerator.generateAndSave()
        val randomBuyingOrderDto: BuyingOrderDto = fluent.request { get { "/buying_order/${buyingOrder.uuid}" } }
        randomBuyingOrderDto.description!!.shouldNotBeEmpty()
    }

    @Test
    fun getTruckByOrder() {
        val buyingOrders = buyingOrderGenerator.generateAndSave(100)
        val uri = UrlBuilder.fromString("/buying_order/${buyingOrders.random().uuid}/truck")
            .addParameter(RestValues.PAGE, page.toString())
            .addParameter(RestValues.SIZE, pageSize.toString())
            .toString()
        val truckDtoList: List<TruckDto> = fluent.request { get { uri } }
        truckDtoList.shouldNotBeEmpty()
    }
}
