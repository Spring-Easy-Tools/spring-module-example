package ru.virgil.spring.example.security.v2

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import ru.virgil.spring.tools.util.data.Timed
import java.time.ZonedDateTime

@Entity
class SecurityUserV2(
    @Id
    var id: String,
    @ElementCollection(String::class, FetchType.EAGER)
    var roles: Set<String>,
    secret: String? = null,
    // todo: нужно ли? Если все равно берется из authentication.name
    var principalName: String? = null,
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
            if (value != null && !Regex("""^\{\w+}""").containsMatchIn(value)) {
                throw SecurityException("Пароль должен быть захешированным и содержать префикс вида {bcrypt}, {noop} и т.д.")
            }
            field = value
        }

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    override fun getAttributes(): Map<String?, Any?>? = oAuth2Attributes

    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it) }

    override fun getPassword() = secret

    override fun getUsername() = id

    override fun getName(): String? = principalName

    override fun getClaims(): Map<String?, Any?>? = oidcClaims

    override fun getUserInfo(): OidcUserInfo? = oidcUserInfo

    override fun getIdToken(): OidcIdToken? = oidcIdToken
}
