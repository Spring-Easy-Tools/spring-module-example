package ru.virgil.spring.example.image

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.example.security.SecurityUser
import ru.virgil.spring.tools.image.PrivateImageInterface
import ru.virgil.spring.tools.util.data.Soft
import ru.virgil.spring.tools.util.data.Timed
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*

@EntityListeners(AuditingEntityListener::class)
@Entity
class PrivateImageFile(fileLocation: Path) : Timed, PrivateImageInterface, Soft {

    @get:Transient
    override var fileLocation: Path
        get() = Path.of(location)
        set(value) {
            location = value.toString()
        }

    private var location: String = fileLocation.toString()

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
