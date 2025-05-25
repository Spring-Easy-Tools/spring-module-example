package ru.virgil.spring.example.order

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.tools.entity.Columns
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.entity.Identified
import ru.virgil.spring.tools.entity.Owned
import ru.virgil.spring.tools.entity.Soft
import ru.virgil.spring.tools.entity.Timed
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class BuyingOrder(
    @ManyToOne(cascade = [CascadeType.ALL])
    var truck: Truck,
    @Column(columnDefinition = Columns.text)
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
    override lateinit var createdBy: String

    override var deleted: Boolean = false
}
