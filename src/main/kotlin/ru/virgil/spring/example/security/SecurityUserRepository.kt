package ru.virgil.spring.example.security

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.tools.Deprecation
import java.util.*

@Deprecated(Deprecation.NEW_NATIVE_AUTH)
@Repository
interface SecurityUserRepository : CrudRepository<SecurityUser, UUID> {

    fun findByFirebaseUserId(firebaseUserId: String): SecurityUser?

    fun findBySpringUsername(username: String): SecurityUser?
}
