package ru.virgil.spring.example.system.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import ru.virgil.spring.tools.Deprecation
import ru.virgil.spring.tools.util.data.Owned
import java.util.*

@Deprecated(Deprecation.TOO_TIGHT)
@NoRepositoryBean
interface OwnedRepository<Entity : Owned> : JpaRepository<Entity, UUID> {

    fun findAllByCreatedBy(createdBy: String): List<Entity>
}
