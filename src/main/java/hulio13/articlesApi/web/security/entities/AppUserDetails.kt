package hulio13.articlesApi.web.security.entities

import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
open class AppUserDetails(
    @Id
    @Column(name = "Username")
    private var username: String,
    @Column(name = "password", nullable = false)
    private var password: String,
    @Column(name = "is_enabled", nullable = false)
    private var enabled: Boolean = true,
    @Column(name = "is_expired", nullable = false)
    private var expired: Boolean = false,
    @Column(name = "is_locked", nullable = false)
    private var locked: Boolean = false,
    @Column(name = "email_confirmed", nullable = false)
    private var emailConfirmed: Boolean = false,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    private val roles: MutableList<Role> = mutableListOf(),
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val author: Author = Author(AuthorName("Placeholder"))
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        roles.stream().map { SimpleGrantedAuthority(it.role) }.toList()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = !expired

    override fun isAccountNonLocked(): Boolean = !locked

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled

    fun removeRole(role: String): Boolean = roles.removeIf { it.role == role }

    fun addRoleIfNotExists(role: String) {
        val tmpRole = roles.stream().filter { it.role == role }.findFirst();

        if (tmpRole.isEmpty) {
            roles.add(Role(role))
        }
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getRoles(): List<Role> = roles
}