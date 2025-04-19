package ru.virgil.spring.example.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import ru.virgil.spring.example.security.v2.OAuth2ToUserDetailsV2Service
import ru.virgil.spring.example.security.v2.OidcToUserDetailsV2Service
import ru.virgil.spring.tools.security.Security
import ru.virgil.spring.tools.security.SecurityProperties


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val securityProperties: SecurityProperties,
    private val oAuth2ToUserDetailsService: OAuth2ToUserDetailsV2Service,
    private val oidcToUserDetailsV2Service: OidcToUserDetailsV2Service,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers(*securityProperties.anonymousPaths.toTypedArray()).permitAll()
                it.requestMatchers(*Security.defaultPublicPaths).permitAll()
                it.anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .oauth2Login { configurer ->
                configurer.userInfoEndpoint { userInfoConfig ->
                    userInfoConfig.userService(oAuth2ToUserDetailsService)
                    userInfoConfig.oidcUserService(oidcToUserDetailsV2Service)
                }
            }
            .formLogin(Customizer.withDefaults())
            .build()
    }
}
