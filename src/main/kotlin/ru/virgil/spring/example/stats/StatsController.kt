package ru.virgil.spring.example.stats

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.virgil.spring.example.box.BoxRepository
import ru.virgil.spring.example.box.BoxService
import ru.virgil.spring.example.order.BuyingOrderRepository
import ru.virgil.spring.example.order.BuyingOrderService
import ru.virgil.spring.example.security.Cors
import ru.virgil.spring.example.truck.TruckRepository

@Cors
@RestController
@RequestMapping("/stats")
class StatsController(
    private val boxService: BoxService,
    private val boxRepository: BoxRepository,
    private val truckRepository: TruckRepository,
    private val buyingOrderRepository: BuyingOrderRepository,
    private val buyingOrderService: BuyingOrderService,
) {

    @GetMapping("/all")
    fun getAllStats(): StatsDto {
        val boxesCount = boxRepository.count()
        val trucksCount = truckRepository.count()
        val ordersCount = buyingOrderRepository.count()
        return StatsDto(boxesCount, trucksCount, ordersCount)
    }

    @GetMapping("/my")
    fun getMyStats(): StatsDto {
        val boxesCount = boxService.countMy()
        val trucksCount = truckRepository.count()
        val ordersCount = buyingOrderService.countMy()
        return StatsDto(boxesCount, trucksCount, ordersCount)
    }

}
