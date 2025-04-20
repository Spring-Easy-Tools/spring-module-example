package ru.virgil.spring.example.order

import org.springframework.web.bind.annotation.*
import ru.virgil.spring.example.box.BoxService
import ru.virgil.spring.example.truck.TruckDto
import ru.virgil.spring.example.truck.TruckMapper
import ru.virgil.spring.example.truck.TruckService
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orNotFound
import java.util.*

@GlobalCors
@RestController
@RequestMapping("/buying_order")
class BuyingOrderController(
    private val buyingOrderService: BuyingOrderService,
    private val truckService: TruckService,
    override val boxService: BoxService,
) : BuyingOrderMapper, TruckMapper {

    @GetMapping
    fun getAll(@RequestParam page: Int, @RequestParam size: Int) = buyingOrderService.getAll(page, size)
        .map { it.toDto() }

    @GetMapping("/{uuid}")
    operator fun get(@PathVariable uuid: UUID): BuyingOrderDto {
        val buyingOrder = buyingOrderService.get(uuid).orNotFound()
        return buyingOrder.toDto()
    }

    @GetMapping("/{buyingOrderUuid}/truck")
    fun getTrucksByOrder(
        @PathVariable buyingOrderUuid: UUID,
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): List<TruckDto> {
        val buyingOrder = buyingOrderService.get(buyingOrderUuid).orNotFound()
        val trucks = truckService.getAll(buyingOrder, page, size)
        return trucks.map { it.toDto() }
    }
}
