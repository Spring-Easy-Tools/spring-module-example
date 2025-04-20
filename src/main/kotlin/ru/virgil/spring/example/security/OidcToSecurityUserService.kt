package ru.virgil.spring.example.security

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.oauth.OAuthToUserMapper

@Service
class OidcToSecurityUserService(
    override val userDetailsManager: SecurityUserManager,
) : OAuth2UserService<OidcUserRequest, OidcUser>, OAuthToUserMapper<OidcUserRequest, SecurityUser> {

    override fun loadUser(userRequest: OidcUserRequest) = findOrMapUser(userRequest)

    override fun findExistingUser(userRequest: OidcUserRequest) =
        userDetailsManager.loadUserByUsername(userRequest.idToken.email) as SecurityUser?

    override fun mapNewUser(userRequest: OidcUserRequest) = SecurityUser(
        id = userRequest.idToken.email,
        roles = setOf(SecurityRole.ROLE_USER.name, *userRequest.accessToken.scopes.toTypedArray()),
        oidcUserInfo = OidcUserInfo(userRequest.idToken.claims),
        oidcIdToken = userRequest.idToken,
        oidcClaims = userRequest.idToken.claims,
        oAuth2Attributes = userRequest.additionalParameters,
        oAuth2PrincipalName = userRequest.idToken.subject,
    )
}
