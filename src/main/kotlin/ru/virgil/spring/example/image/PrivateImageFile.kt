package ru.virgil.spring.example.image

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Transient
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ru.virgil.spring.tools.image.PrivateImageInterface
import ru.virgil.spring.tools.entity.Soft
import ru.virgil.spring.tools.entity.Timed
import java.nio.file.Path
import java.time.ZonedDateTime
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

    /** Нельзя ставить аннотацию @GeneratedValue, из-за неё начинается конфликт строк в БД */
    @Id
    override lateinit var uuid: UUID

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    @CreatedBy
    override lateinit var createdBy: String

    override var deleted: Boolean = false
}
