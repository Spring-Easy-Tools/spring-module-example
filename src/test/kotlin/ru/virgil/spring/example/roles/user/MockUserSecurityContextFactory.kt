package ru.virgil.spring.example.roles.user

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.v2.SecurityUserV2
import ru.virgil.spring.example.security.v2.SecurityUserV2Manager
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory

@Component
class MockUserSecurityContextFactory(
    authenticationEventPublisher: AuthenticationEventPublisher,
    securityUserV2Manager: SecurityUserV2Manager,
) : WithSecurityContextFactory<WithMockedUser>, MockSecurityContextFactory(
    authenticationEventPublisher = authenticationEventPublisher,
    userDetailsManager = securityUserV2Manager
) {

    override fun createSecurityContext(annotation: WithMockedUser) = mockSecurityContext {
        SecurityUserV2(
            id = annotation.userId,
            secret = annotation.userSecret,
            roles = annotation.authorities.map { it.name }.toSet()
        )
    }
}
