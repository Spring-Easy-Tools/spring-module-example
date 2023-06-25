package ru.virgil.spring.example.mock

import org.springframework.data.repository.CrudRepository
import ru.virgil.spring.example.security.SecurityUser

typealias MockerRepository<Entity, Id> = CrudRepository<Entity, Id>

/**
 * Желательно, чтобы мокер использовал все свое и был независим от остальной системы и бизнес-логики.
 * */
interface EntityMocker<Entity> {

    val authHandler: MockAuthSuccessHandler
    val repository: MockerRepository<Entity, *>

    val principal: SecurityUser
        get() = authHandler.principal

    fun new(): Entity
    fun random(): Entity
}
