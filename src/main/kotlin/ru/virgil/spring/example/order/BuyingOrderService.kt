package ru.virgil.spring.example.order

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.Security.getCreator
import java.util.*

@Service
class BuyingOrderService(
    private val buyingOrderRepository: BuyingOrderRepository,
) {

    fun getAll(page: Int, size: Int) =
        buyingOrderRepository.findAllByCreatedBy(getCreator(), PageRequest.of(page, size))

    fun get(uuid: UUID) =
        buyingOrderRepository.findByCreatedByAndUuid(getCreator(), uuid)

    fun countMy() = buyingOrderRepository.countAllByCreatedBy(getCreator())
}
