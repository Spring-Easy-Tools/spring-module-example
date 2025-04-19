package ru.virgil.spring.example.security.v2

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import ru.virgil.spring.example.security.SecurityRole

@Service
class OidcToUserDetailsV2Service(
    private val userDetailsManager: SecurityUserV2Manager,
) : OAuth2UserService<OidcUserRequest, OidcUser> {

    override fun loadUser(oidcUserRequest: OidcUserRequest): OidcUser {
        val securityUser = userDetailsManager.loadUserByUsername(oidcUserRequest.idToken.email)
            ?: registerAsSecurityUser(oidcUserRequest)
        return securityUser as OidcUser
    }

    private fun registerAsSecurityUser(oidcUserRequest: OidcUserRequest): SecurityUserV2 {
        val securityUserV2 = SecurityUserV2(
            id = oidcUserRequest.idToken.email,
            roles = setOf(SecurityRole.ROLE_USER.name, *oidcUserRequest.accessToken.scopes.toTypedArray()),
            oidcUserInfo = OidcUserInfo(oidcUserRequest.idToken.claims),
            oidcIdToken = oidcUserRequest.idToken,
            oidcClaims = oidcUserRequest.idToken.claims,
            oAuth2Attributes = oidcUserRequest.additionalParameters,
            principalName = oidcUserRequest.idToken.subject,
        )
        userDetailsManager.createUser(securityUserV2)
        return securityUserV2
    }
}
