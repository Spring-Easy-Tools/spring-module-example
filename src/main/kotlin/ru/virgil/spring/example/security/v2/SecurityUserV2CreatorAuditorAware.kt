package ru.virgil.spring.example.security.v2

import org.springframework.data.domain.AuditorAware
import java.util.*

// @Component
class SecurityUserV2CreatorAuditorAware : AuditorAware<SecurityUserV2> {

    override fun getCurrentAuditor() = Optional.of(ExtendedSecurity.getSecurityUserCreator())
}