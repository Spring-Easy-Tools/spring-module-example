package ru.virgil.spring.example.mock

import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityUser

/**
 * Этот хендлер отвечает за запуск генератора моков после прохождения авторизации
 * */
@Component
class MockAuthSuccessHandler(val mockGenerator: MockGenerator, val context: ApplicationContext) {

    lateinit var principal: SecurityUser

    @EventListener
    fun onSuccess(success: AuthenticationSuccessEvent?) {
        // TODO: ForbiddenToken, несмотря на isAuthenticated = false, все равно приходит сюда,
        //    поэтому дополнительно отслеживаем
        if (!success!!.authentication.isAuthenticated) return
        principal = success.authentication.principal as SecurityUser
        // TODO: Вынести логику управления запуском мокера сюда
        mockGenerator.start(principal)
    }
}
