package ru.virgil.spring.example.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.Jwt
import ru.virgil.spring.tools.Deprecation
import ru.virgil.spring.tools.security.oauth.SecurityUserService

@Deprecated(Deprecation.NEW_NATIVE_AUTH)
// @Primary
// @Service
class SecurityUserService(
    private val securityUserRepository: SecurityUserRepository,
) : SecurityUserService {

    // @get:Primary
    // @get:Lazy
    // @get:Bean(current)
    // override val principal: UserDetails
    //     get() = super.principal

    // @get:Primary
    // @get:Lazy
    // override val token: Authentication
    //     get() = super.token

    override fun loadByFirebaseUserId(firebaseUserId: String): SecurityUser? =
        securityUserRepository.findByFirebaseUserId(firebaseUserId)

    override fun loadUserByUsername(username: String): SecurityUser? =
        securityUserRepository.findBySpringUsername(username)

    override fun registerByFirebaseUserId(firebaseUserId: String, authToken: Jwt): UserDetails {
        val newFirebaseUser = SecurityUser(firebaseUserId, mutableSetOf(SecurityRole.ROLE_USER))
        return securityUserRepository.save(newFirebaseUser)
    }

    companion object {

        const val name = "security-user"
        const val current = "current-$name"
        const val mocking = "mocking-time-$name"
    }
}
