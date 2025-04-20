package ru.virgil.spring.example.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager
import ru.virgil.spring.tools.websocket.WebSocketProperties

@Configuration
@EnableWebSocketSecurity
class WebSocketSecurityConfig(
    private val webSocketProperties: WebSocketProperties,
) {

    @Bean
    fun authorizationManager(messages: MessageMatcherDelegatingAuthorizationManager.Builder) = messages
        .let {
            if (webSocketProperties.publicDestinations.isNotEmpty()) {
                it.simpDestMatchers(*webSocketProperties.publicDestinations.toTypedArray()).permitAll()
            } else it
        }
        .simpDestMatchers("/police_channel/**").hasAuthority(SecurityRole.ROLE_POLICE.name)
        .anyMessage().authenticated()
        .build()
}
