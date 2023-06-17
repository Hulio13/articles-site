package hulio13.articlesApi.web.security.entities;

import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUserDetails implements UserDetails {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;
    @Column(name = "is_expired", nullable = false)
    private boolean expired = false;
    @Column(name = "is_locked", nullable = false)
    private boolean locked = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Role> roles = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Author author = new Author(new AuthorName("Placeholder"));

    public void addRoleIfNotExist(String role){
        Role tmpRole = new Role(role);

        if (!roles.contains(tmpRole)){
            roles.add(tmpRole);
        }
    }

    public boolean removeRole(String role){
        return roles.remove(new Role(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
