package ru.virgil.spring.example.roles.police

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityUser
import ru.virgil.spring.example.security.SecurityUserManager
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory

@Component
class MockPoliceSecurityContextFactory(
    authenticationEventPublisher: AuthenticationEventPublisher,
    securityUserManager: SecurityUserManager,
) : WithSecurityContextFactory<WithMockedPoliceman>, MockSecurityContextFactory(securityUserManager) {

    override fun createSecurityContext(annotation: WithMockedPoliceman) = mockSecurityContext {
        SecurityUser(
            id = annotation.userId,
            roles = annotation.authorities.map { it.name }.toSet(),
            secret = annotation.userSecret,
        )
    }
}
