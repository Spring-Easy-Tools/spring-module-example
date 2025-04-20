package ru.virgil.spring.example.security.v2

import jakarta.annotation.PostConstruct
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component
import ru.virgil.spring.tools.security.user.DefaultUserProperties
import ru.virgil.spring.tools.security.Security
import ru.virgil.spring.tools.util.Http.orNotFound
import ru.virgil.spring.tools.util.Http.thenConflict
import kotlin.jvm.optionals.getOrNull

@Component
class SecurityUserV2Manager(
    private val repository: SecurityUserV2Repository,
    private val defaultUserProperties: DefaultUserProperties?,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsManager {

    // todo: вынести в какой-то подключаемый блок вместе с маппингом пропертис
    @PostConstruct
    fun initDefaultUser() {
        if (defaultUserProperties == null) return
        if (!userExists(defaultUserProperties.name!!)) {
            val securityUserV2 = SecurityUserV2(
                id = defaultUserProperties.name!!,
                roles = defaultUserProperties.roles!!.toSet(),
                secret = passwordEncoder.encode(defaultUserProperties.password!!)
            )
            createUser(securityUserV2)
        }
    }

    override fun createUser(user: UserDetails) {
        val user = user as SecurityUserV2
        repository.findByIdOrNull(user.id).thenConflict()
        repository.save(user)
    }

    override fun updateUser(user: UserDetails) {
        val user = user as SecurityUserV2
        repository.findByIdOrNull(user.id).orNotFound()
        repository.save(user)
    }

    override fun deleteUser(username: String) {
        repository.deleteById(username)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        val principal = Security.getAuthentication().principal as SecurityUserV2
        // Проверяем старый пароль через passwordEncoder
        if (!passwordEncoder.matches(oldPassword, principal.secret)) {
            throw SecurityException("Старый пароль неверен")
        }
        // Хешируем новый пароль
        principal.secret = passwordEncoder.encode(newPassword)
        updateUser(principal)
    }

    override fun userExists(username: String): Boolean {
        return repository.existsById(username)
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return repository.findById(username).getOrNull()
    }
}
