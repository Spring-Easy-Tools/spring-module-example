package ru.virgil.spring.example.order

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import java.util.*

@Service
class BuyingOrderService(
    // @Qualifier(UserDetailsService.current)
    // private val ownerProvider: ObjectProvider<UserDetails>,
    private val buyingOrderRepository: BuyingOrderRepository,
    // securityUserService: SecurityUserService,
) {

    // private val owner by lazy { ownerProvider.getObject() }
    // private val securityUser by lazy { securityUserService.principal }
    // val securityUser by lazy { getPrincipal()}

    fun getAll(page: Int, size: Int): List<BuyingOrder> =
        buyingOrderRepository.findAllByCreatedBy(getSimpleCreator(), PageRequest.of(page, size))

    fun get(uuid: UUID): BuyingOrder =
        buyingOrderRepository.findByCreatedByAndUuid(getSimpleCreator(), uuid).orElseThrow()

    fun countMy(): Long = buyingOrderRepository.countAllByCreatedBy(getSimpleCreator())

}
