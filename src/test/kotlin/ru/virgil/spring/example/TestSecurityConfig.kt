package ru.virgil.spring.example

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository

@Configuration
class TestSecurityConfig {

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return Mockito.mock(ClientRegistrationRepository::class.java)
    }
}
