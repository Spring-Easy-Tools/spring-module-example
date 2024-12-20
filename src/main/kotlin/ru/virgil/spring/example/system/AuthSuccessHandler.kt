package ru.virgil.spring.example.system

import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component
import ru.virgil.spring.example.security.SecurityUser

/**
 * Тут можно делать вещи сразу после авторизации
 * */
@Component
class AuthSuccessHandler(
    val context: ApplicationContext,
) {

    lateinit var principal: SecurityUser

    @EventListener
    fun onSuccess(success: AuthenticationSuccessEvent?) {
        // TODO: ForbiddenToken, несмотря на isAuthenticated = false, все равно приходит сюда,
        //    поэтому дополнительно отслеживаем
        if (!success!!.authentication.isAuthenticated) return
        principal = success.authentication.principal as SecurityUser
    }
}
