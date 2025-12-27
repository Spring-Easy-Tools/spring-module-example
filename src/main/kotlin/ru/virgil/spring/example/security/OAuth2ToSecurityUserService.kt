package ru.virgil.spring.example.security

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.security.oauth.OAuthToUserMapper

@Service
class OAuth2ToSecurityUserService(
    override val userDetailsManager: SecurityUserManager,
) : DefaultOAuth2UserService(), OAuthToUserMapper<OAuth2UserRequest, SecurityUser> {

    override fun loadUser(userRequest: OAuth2UserRequest) = findOrMapUser(userRequest)

    override fun findExistingUser(userRequest: OAuth2UserRequest): SecurityUser? = try {
        userDetailsManager.loadUserByUsername(userRequest.clientRegistration.clientId) as SecurityUser?
    } catch (_: UsernameNotFoundException) {
        null
    }

    override fun mapUser(userRequest: OAuth2UserRequest) = SecurityUser(
        id = userRequest.clientRegistration.clientId,
        roles = setOf(SecurityRole.ROLE_USER.name, *userRequest.clientRegistration.scopes.toTypedArray()),
        oauthUser = super.loadUser(userRequest),
    )
}
