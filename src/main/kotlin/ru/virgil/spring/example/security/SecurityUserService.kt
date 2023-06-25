package ru.virgil.spring.example.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.oauth.SecurityUserService

@Primary
@Service
class SecurityUserService(
    private val securityUserRepository: SecurityUserRepository,
) : SecurityUserService {

    @get:Primary
    @get:Lazy
    @get:Bean(current)
    override val principal: UserDetails
        get() = super.principal

    @get:Primary
    @get:Lazy
    override val token: Authentication
        get() = super.token

    override fun loadByFirebaseUserId(firebaseUserId: String): SecurityUser? =
        securityUserRepository.findByFirebaseUserId(firebaseUserId)

    override fun loadUserByUsername(username: String): SecurityUser? =
        securityUserRepository.findBySpringUsername(username)

    override fun registerByFirebaseUserId(firebaseUserId: String, authToken: Jwt): UserDetails {
        val newFirebaseUser = SecurityUser(firebaseUserId, mutableSetOf(SecurityUserAuthority.ROLE_USER))
        return securityUserRepository.save(newFirebaseUser)
    }

    companion object {

        const val name = "security-user"
        const val current = "current-$name"
        const val mocking = "mocking-time-$name"
    }
}
