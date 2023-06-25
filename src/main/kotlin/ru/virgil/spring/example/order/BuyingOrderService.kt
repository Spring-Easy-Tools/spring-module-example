package ru.virgil.spring.example.order

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.security.SecurityUserService
import java.util.*

@Service
class BuyingOrderService(
    // @Qualifier(UserDetailsService.current)
    // private val ownerProvider: ObjectProvider<UserDetails>,
    private val buyingOrderRepository: BuyingOrderRepository,
    securityUserService: SecurityUserService,
) {

    // private val owner by lazy { ownerProvider.getObject() }
    private val securityUser by lazy { securityUserService.principal }

    fun getAll(page: Int, size: Int): List<BuyingOrder> =
        buyingOrderRepository.findAllByCreatedBy(securityUser, PageRequest.of(page, size))

    fun get(uuid: UUID): BuyingOrder = buyingOrderRepository.findByCreatedByAndUuid(securityUser, uuid).orElseThrow()

    fun countMy(): Long = buyingOrderRepository.countAllByCreatedBy(securityUser)

}
