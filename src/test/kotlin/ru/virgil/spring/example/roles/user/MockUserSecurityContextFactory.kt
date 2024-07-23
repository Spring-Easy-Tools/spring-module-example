package ru.virgil.spring.example.roles.user

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityUserService
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory

@Component
class MockUserSecurityContextFactory(
    securityUserService: SecurityUserService,
    authenticationEventPublisher: AuthenticationEventPublisher,
) : WithSecurityContextFactory<WithMockFirebaseUser>,
    MockSecurityContextFactory(securityUserService, authenticationEventPublisher) {

    override fun createSecurityContext(annotation: WithMockFirebaseUser): SecurityContext =
        createSecurityContext(annotation.firebaseUserId, annotation.authorities.map { SimpleGrantedAuthority(it.name) })
}
