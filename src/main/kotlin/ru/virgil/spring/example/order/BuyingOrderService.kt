package ru.virgil.spring.example.order

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import java.util.*

@Service
class BuyingOrderService(
    private val buyingOrderRepository: BuyingOrderRepository,
) {

    fun getAll(page: Int, size: Int) =
        buyingOrderRepository.findAllByCreatedBy(getSimpleCreator(), PageRequest.of(page, size))

    fun get(uuid: UUID) =
        buyingOrderRepository.findByCreatedByAndUuid(getSimpleCreator(), uuid)

    fun countMy() = buyingOrderRepository.countAllByCreatedBy(getSimpleCreator())
}
