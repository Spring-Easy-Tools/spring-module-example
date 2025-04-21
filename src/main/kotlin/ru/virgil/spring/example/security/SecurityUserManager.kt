package ru.virgil.spring.example.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.virgil.spring.tools.security.user.DefaultUserProperties
import ru.virgil.spring.tools.security.user.JpaUserDetailsManager

@Component
class SecurityUserManager(
    repository: SecurityUserRepository,
    defaultUserProperties: DefaultUserProperties?,
    passwordEncoder: PasswordEncoder,
) : JpaUserDetailsManager(repository as JpaRepository<UserDetails, String>, defaultUserProperties, passwordEncoder) {

    override fun mapPropertiesToUsed(defaultUserProperties: DefaultUserProperties): SecurityUser {
        val properties = defaultUserProperties
        return SecurityUser(
            id = properties.name!!,
            roles = properties.roles!!.toSet(),
            secret = passwordEncoder.encode(properties.password!!),
        )
    }

    override fun applyNewPassword(principal: UserDetails, encodedNewPassword: String) {
        val securityUser = principal as SecurityUser
        securityUser.secret = encodedNewPassword
    }
}
