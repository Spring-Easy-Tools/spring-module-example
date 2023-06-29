package ru.virgil.spring.example

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import org.apache.http.client.utils.URIBuilder
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

@DirtiesContext
@SpringBootTest
@ComponentScan(basePackage)
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
            get { "/buying_order?${RestValues.pageParam}=$page&${RestValues.pageSizeParam}=$pageSize" }
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
        val uri = URIBuilder().setPathSegments("buying_order", buyingOrder.uuid.toString(), "truck")
            .addParameter(RestValues.pageParam, page.toString())
            .addParameter(RestValues.pageSizeParam, pageSize.toString())
            .toString()
        val truckDtoList: List<TruckDto> = fluent.request { get { uri } }
        truckDtoList.shouldNotBeEmpty()
    }
}
