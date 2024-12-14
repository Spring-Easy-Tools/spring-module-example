package ru.virgil.spring.example.order

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.example.security.SecurityUser
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.util.data.Identified
import ru.virgil.spring.tools.util.data.Owned
import ru.virgil.spring.tools.util.data.Soft
import ru.virgil.spring.tools.util.data.Timed
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class BuyingOrder(
    @ManyToOne
    var truck: Truck,
    var description: String?,
) : Owned, Identified, Timed, Soft {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    @CreatedBy
    @ManyToOne
    override lateinit var createdBy: SecurityUser

    override var deleted: Boolean = false
}
