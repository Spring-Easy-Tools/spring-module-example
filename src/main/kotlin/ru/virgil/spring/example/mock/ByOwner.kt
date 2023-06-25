package ru.virgil.spring.example.mock

import org.springframework.security.core.userdetails.UserDetails
import ru.virgil.spring.tools.util.data.Owned

interface ByOwner<Entity : Owned> {

    fun findAllByCreatedBy(createdBy: UserDetails): Collection<Entity>
}
