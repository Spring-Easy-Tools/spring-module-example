package ru.virgil.spring.example.security

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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
    val oauthUser: OAuth2User? = null,
) : UserDetails, OAuth2User, OidcUser, Timed {

    var secret = secret
        set(value) {
            field = value.checkSecretHashed()
        }

    @CreationTimestamp
    override lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    override lateinit var updatedAt: ZonedDateTime

    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it) }

    override fun getPassword() = secret

    override fun getUsername() = id

    override fun getAttributes() = oauthUser!!.attributes

    override fun getName() = oauthUser!!.name

    override fun getClaims() = (oauthUser as OidcUser).claims

    override fun getUserInfo() = (oauthUser as OidcUser).userInfo

    override fun getIdToken() = (oauthUser as OidcUser).idToken
}
