package ru.virgil.spring.example.security

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.oauth.OAuthToUserMapper

@Service
class OidcToSecurityUserService(
    override val userDetailsManager: SecurityUserManager,
) : OidcUserService(), OAuthToUserMapper<OidcUserRequest, SecurityUser> {

    override fun loadUser(userRequest: OidcUserRequest) = findOrMapUser(userRequest)

    override fun findExistingUser(userRequest: OidcUserRequest) =
        userDetailsManager.loadUserByUsername(userRequest.idToken.email) as SecurityUser?

    override fun mapUser(userRequest: OidcUserRequest) = SecurityUser(
        id = userRequest.idToken.email,
        roles = setOf(SecurityRole.ROLE_USER.name, *userRequest.accessToken.scopes.toTypedArray()),
        oauthUser = super.loadUser(userRequest)
    )
}
