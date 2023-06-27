package ru.virgil.spring.example.box

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
import java.time.LocalDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Box(
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: BoxType = BoxType.USUAL,
    @ManyToOne(cascade = [CascadeType.REMOVE])
    var truck: Truck,
    var description: String,
    var price: Int = 0,
    var weight: Float = 0f,
) : Owned, Identified, Timed, Soft {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: LocalDateTime

    @CreatedBy
    @ManyToOne
    override lateinit var createdBy: SecurityUser

    override var deleted: Boolean = false
}
