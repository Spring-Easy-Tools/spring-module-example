package ru.virgil.spring.example.security

import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import ru.virgil.spring.tools.security.Security
import ru.virgil.spring.tools.security.SecurityProperties


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
class SecurityConfig(
    private val securityProperties: SecurityProperties,
    private val oAuth2ToSecurityUserService: OAuth2ToSecurityUserService,
    private val oidcToSecurityUserService: OidcToSecurityUserService,
    private val clientRegistrationRepositoryProvider: ObjectProvider<ClientRegistrationRepository>,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it.requestMatchers(*securityProperties.anonymousPaths.toTypedArray()).permitAll()
                it.requestMatchers(*Security.defaultPublicPaths).permitAll()
                it.anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())

        // Создаем конфигурацию OAuth2 логина только если есть креденшиалсы OAuth2 клиентов
        val clientRegistrationRepository = clientRegistrationRepositoryProvider.getIfAvailable()
        if (clientRegistrationRepository != null) {
            http.oauth2Login { configurer ->
                configurer.userInfoEndpoint { userInfoConfig ->
                    userInfoConfig.userService(oAuth2ToSecurityUserService)
                    userInfoConfig.oidcUserService(oidcToSecurityUserService)
                }
            }
        }

        return http.build()
    }
}
