package ru.virgil.spring.example.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BuyingOrderGeneratorRepository : JpaRepository<BuyingOrder, UUID>
