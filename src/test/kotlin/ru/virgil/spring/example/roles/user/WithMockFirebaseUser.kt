package ru.virgil.spring.example.roles.user

import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import ru.virgil.spring.example.security.SecurityRole

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_METHOD)
annotation class WithMockFirebaseUser(
    val authorities: Array<SecurityRole> = [SecurityRole.ROLE_USER],
    val firebaseUserId: String = "user-id",
    val firebaseAuthToken: String = "user-auth-token",
)
