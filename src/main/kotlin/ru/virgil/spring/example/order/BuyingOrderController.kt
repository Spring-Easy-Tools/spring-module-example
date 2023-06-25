package ru.virgil.spring.example.order

import org.springframework.web.bind.annotation.*
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.example.truck.TruckDto
import ru.virgil.spring.example.truck.TruckMapper
import ru.virgil.spring.example.truck.TruckService
import ru.virgil.spring.tools.security.cors.GlobalCors
import java.util.*

@GlobalCors
@RestController
@RequestMapping("/buying_order")
class BuyingOrderController(
    private val buyingOrderService: BuyingOrderService,
    private val truckService: TruckService,
) : BuyingOrderMapper, TruckMapper {

    @GetMapping
    fun getAll(
        @RequestParam(RestValues.page) page: Int,
        @RequestParam(RestValues.size) size: Int,
    ): List<BuyingOrderDto> =
        buyingOrderService.getAll(page, size).stream()
            .map { it.toDto() }
            .toList()

    @GetMapping("/{uuid}")
    operator fun get(@PathVariable uuid: UUID): BuyingOrderDto {
        val buyingOrder = buyingOrderService.get(uuid)
        return buyingOrder.toDto()
    }

    @GetMapping("/{buyingOrderUuid}/truck")
    fun getTrucksByOrder(
        @PathVariable buyingOrderUuid: UUID,
        @RequestParam(RestValues.page) page: Int,
        @RequestParam(RestValues.size) size: Int,
    ): List<TruckDto> {
        val buyingOrder = buyingOrderService.get(buyingOrderUuid)
        val trucks = truckService.getAll(buyingOrder, page, size)
        return trucks.stream()
            .map { it.toDto() }
            .toList()
    }
}
