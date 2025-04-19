package ru.virgil.spring.example.roles.police

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityRole
import ru.virgil.spring.example.security.v2.SecurityUserV2
import ru.virgil.spring.example.security.v2.SecurityUserV2Manager
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory

@Component
class MockPoliceSecurityContextFactory(
    authenticationEventPublisher: AuthenticationEventPublisher,
    securityUserV2Manager: SecurityUserV2Manager,
) : WithSecurityContextFactory<WithMockFirebasePoliceman>, MockSecurityContextFactory(
    authenticationEventPublisher = authenticationEventPublisher,
    userDetailsManager = securityUserV2Manager
) {

    override fun createSecurityContext(annotation: WithMockFirebasePoliceman) = mockSecurityContext {
        SecurityUserV2(
            id = mockedName,
            secret = mockedPassword,
            roles = setOf(SecurityRole.ROLE_POLICE.name)
        )
    }
}
