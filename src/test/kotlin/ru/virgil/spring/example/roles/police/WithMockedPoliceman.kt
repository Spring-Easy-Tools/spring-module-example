package ru.virgil.spring.example.roles.police

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import ru.virgil.spring.example.security.SecurityRole

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockPoliceSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockedPoliceman(
    val authorities: Array<SecurityRole> = [SecurityRole.ROLE_POLICE],
    val userId: String = "police-id",
    val userSecret: String = "police-auth-token",
)
