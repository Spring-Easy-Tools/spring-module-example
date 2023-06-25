package ru.virgil.spring.example.system.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.security.core.userdetails.UserDetails
import ru.virgil.spring.tools.util.data.Owned

import java.util.*

@NoRepositoryBean
interface OwnedRepository<Entity : Owned> : JpaRepository<Entity, UUID> {

    fun findAllByCreatedBy(createdBy: UserDetails): List<Entity>
}
