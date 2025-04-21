package ru.virgil.spring.example.chat

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.tools.entity.Identified
import ru.virgil.spring.tools.entity.Owned
import ru.virgil.spring.tools.entity.Timed
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ChatMessage(
    val text: String,
    val author: String?,
) : Identified, Timed, Owned {

    @Id
    @GeneratedValue
    override lateinit var uuid: UUID

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @CreatedBy
    override lateinit var createdBy: String
}
