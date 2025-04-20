package ru.virgil.spring.example.truck

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.example.box.Box
import ru.virgil.spring.example.order.BuyingOrder
import ru.virgil.spring.tools.util.data.Identified
import ru.virgil.spring.tools.util.data.Soft
import ru.virgil.spring.tools.util.data.Timed
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Truck(
    @OneToMany(mappedBy = CONNECTION, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var boxes: Set<Box> = HashSet(),
    @OneToMany(mappedBy = CONNECTION, fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val buyingOrder: Set<BuyingOrder> = HashSet(),
) : Identified, Timed, Soft {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    override var deleted: Boolean = false

    companion object {

        /**
         * Это фикс поведения JPA для двусторонних связей. Проблема тянется еще с JPA 2,
         * но ее не фиксят, чтобы не сломать совместимость. Читал на StackOverflow
         */
        const val CONNECTION = "truck"
    }
}
