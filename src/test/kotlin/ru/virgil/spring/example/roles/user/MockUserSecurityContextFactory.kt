package ru.virgil.spring.example.roles.user

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityRole
import ru.virgil.spring.example.security.v2.SecurityUserV2
import ru.virgil.spring.example.security.v2.SecurityUserV2Manager
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory

@Component
class MockUserSecurityContextFactory(
    authenticationEventPublisher: AuthenticationEventPublisher,
    securityUserV2Manager: SecurityUserV2Manager,
) : WithSecurityContextFactory<WithMockFirebaseUser>, MockSecurityContextFactory(
    authenticationEventPublisher = authenticationEventPublisher,
    userDetailsManager = securityUserV2Manager
) {

    override fun createSecurityContext(annotation: WithMockFirebaseUser) = mockSecurityContext {
        SecurityUserV2(
            id = mockedName,
            secret = mockedPassword,
            roles = setOf(SecurityRole.ROLE_USER.name)
        )
    }
}
