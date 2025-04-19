package ru.virgil.spring.example.security.v2

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import ru.virgil.spring.example.security.SecurityRole

@Service
class OAuth2ToUserDetailsV2Service(
    private val userDetailsManager: SecurityUserV2Manager,
) : DefaultOAuth2UserService() {

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val securityUser = userDetailsManager.loadUserByUsername(oAuth2UserRequest.clientRegistration.clientId)
            ?: registerAsSecurityUser(oAuth2UserRequest)
        return securityUser as OAuth2User
    }

    private fun registerAsSecurityUser(oAuth2UserRequest: OAuth2UserRequest): SecurityUserV2 {
        val securityUserV2 = SecurityUserV2(
            id = oAuth2UserRequest.clientRegistration.clientId,
            roles = setOf(SecurityRole.ROLE_USER.name, *oAuth2UserRequest.clientRegistration.scopes.toTypedArray()),
            principalName = oAuth2UserRequest.clientRegistration.clientName,
            oAuth2Attributes = oAuth2UserRequest.additionalParameters,
        )
        userDetailsManager.createUser(securityUserV2)
        return securityUserV2
    }
}
