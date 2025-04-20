package ru.virgil.spring.example.security

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import ru.virgil.spring.tools.entity.Timed
import ru.virgil.spring.tools.security.user.UserSecurity.checkSecretHashed
import java.time.ZonedDateTime

@Entity
class SecurityUser(
    @Id
    var id: String,
    @ElementCollection(String::class, FetchType.EAGER)
    var roles: Set<String>,
    secret: String? = null,
    @Transient
    var oAuth2PrincipalName: String? = null,
    @Transient
    var oAuth2Attributes: Map<String?, *>? = null,
    @Transient
    var oidcClaims: Map<String?, Any?>? = null,
    @Transient
    var oidcUserInfo: OidcUserInfo? = null,
    @Transient
    var oidcIdToken: OidcIdToken? = null,
) : UserDetails, OAuth2User, OidcUser, Timed {

    var secret: String? = secret
        set(value) {
            field = value.checkSecretHashed()
        }

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    override fun getAttributes(): Map<String?, Any?>? = oAuth2Attributes

    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it) }

    override fun getPassword() = secret

    override fun getUsername() = id

    override fun getName(): String? = oAuth2PrincipalName

    override fun getClaims(): Map<String?, Any?>? = oidcClaims

    override fun getUserInfo(): OidcUserInfo? = oidcUserInfo

    override fun getIdToken(): OidcIdToken? = oidcIdToken
}
