package ru.virgil.spring.example.test

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mikael.urlbuilder.UrlBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.example.order.BuyingOrderDto
import ru.virgil.spring.example.order.BuyingOrderMocker
import ru.virgil.spring.example.roles.user.WithMockFirebaseUser
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.example.truck.TruckDto
import ru.virgil.spring.tools.testing.fluent.Fluent
import ru.virgil.spring.tools.toolsBasePackage

@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuyingOrderApiTest @Autowired constructor(
    val fluent: Fluent,
    val buyingOrderMocker: BuyingOrderMocker,
) {

    private val page = 0
    private val pageSize = 10

    @Test
    fun getAll() {
        val buyingOrderDtoList: List<BuyingOrderDto> = fluent.request {
            get { "/buying_order?${RestValues.page}=$page&${RestValues.size}=$pageSize" }
        }
        buyingOrderDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val buyingOrder = buyingOrderMocker.random()
        val randomBuyingOrderDto: BuyingOrderDto = fluent.request { get { "/buying_order/${buyingOrder.uuid}" } }
        randomBuyingOrderDto.description!!.shouldNotBeEmpty()
    }

    @Test
    fun getTruckByOrder() {
        val buyingOrder = buyingOrderMocker.random()
        val uri = UrlBuilder.fromString("/buying_order/${buyingOrder.uuid}/truck")
            .addParameter(RestValues.page, page.toString())
            .addParameter(RestValues.size, pageSize.toString())
            .toString()
        val truckDtoList: List<TruckDto> = fluent.request { get { uri } }
        truckDtoList.shouldNotBeEmpty()
    }
}
