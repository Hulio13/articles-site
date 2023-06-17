package hulio13.articlesApi.web.security;

import hulio13.articlesApi.web.security.entities.AppUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Transactional
public class JPAUserDetailsManager implements UserDetailsManager {
    private JPAUserRepository repository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final PasswordEncoder encoder;

    public JPAUserDetailsManager(JPAUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        try {
            var user = (AppUserDetails) userDetails;
            user.setPassword(encoder.encode(user.getPassword()));
            repository.create(user);
        } catch (ClassCastException e) {
            throw new RuntimeException("User details implementation must be 'User'", e);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        try {
            // TODO: предотвращать изменение пароля
            repository.update((AppUserDetails) user);
        } catch (ClassCastException e) {
            throw new RuntimeException("User details implementation must be 'User'", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        AppUserDetails appUserDetails = repository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found."));

        repository.remove(appUserDetails);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context " + "for current user.");
        }

        AppUserDetails appUserDetails;

        if (currentUser.getPrincipal() instanceof AppUserDetails) {
            appUserDetails = (AppUserDetails) currentUser.getPrincipal();
        } else {
            throw new IllegalCallerException("UserDetails interface implementation should be 'User'");
        }

        String currPassword = appUserDetails.getPassword();
        if (encoder.matches(oldPassword, currPassword)) {
            if (encoder.matches(newPassword, currPassword))
                throw new RuntimeException("Password is equals with old");

            appUserDetails.setPassword(encoder.encode(newPassword));
            repository.update(appUserDetails);
        } else {
            // TODO
            throw new RuntimeException();
        }
    }

    @Override
    public boolean userExists(String username) {
        return repository.getByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found."));
    }
}
