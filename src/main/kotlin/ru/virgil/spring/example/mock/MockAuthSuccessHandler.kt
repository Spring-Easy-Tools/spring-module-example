package ru.virgil.spring.example.mock

import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityUser

@Component
class MockAuthSuccessHandler(val mocker: Mocker, val context: ApplicationContext) {

    lateinit var principal: SecurityUser

    @EventListener
    fun onSuccess(success: AuthenticationSuccessEvent?) {
        principal = success!!.authentication.principal as SecurityUser
        mocker.start()
    }
}
