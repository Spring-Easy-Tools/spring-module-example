package ru.virgil.spring.example.stats

import org.springframework.stereotype.Service
import ru.virgil.spring.example.box.BoxRepository
import ru.virgil.spring.example.box.BoxService
import ru.virgil.spring.example.order.BuyingOrderRepository
import ru.virgil.spring.example.order.BuyingOrderService
import ru.virgil.spring.example.truck.TruckRepository

@Service
class StatsService(
    private val boxService: BoxService,
    private val boxRepository: BoxRepository,
    private val truckRepository: TruckRepository,
    private val buyingOrderRepository: BuyingOrderRepository,
    private val buyingOrderService: BuyingOrderService,
) {

    fun getAllStats(): StatsDto {
        val boxesCount = boxRepository.count()
        val trucksCount = truckRepository.count()
        val ordersCount = buyingOrderRepository.count()
        return StatsDto(boxesCount, trucksCount, ordersCount)
    }

    fun getMyStats(): StatsDto {
        val boxesCount = boxService.countMy()
        val trucksCount = truckRepository.count()
        val ordersCount = buyingOrderService.countMy()
        return StatsDto(boxesCount, trucksCount, ordersCount)
    }
}
