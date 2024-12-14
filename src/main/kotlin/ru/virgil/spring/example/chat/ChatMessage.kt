package ru.virgil.spring.example.chat

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.example.security.SecurityUser
import ru.virgil.spring.tools.util.data.Identified
import ru.virgil.spring.tools.util.data.Owned
import ru.virgil.spring.tools.util.data.Timed
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ChatMessage(
    val text: String,
    // todo: нужно ли, после добавления @CreatedBy?
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
    @ManyToOne
    override lateinit var createdBy: SecurityUser
}
