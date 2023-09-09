package hulio13.articlesApi.web.security;

import hulio13.articlesApi.web.exceptions.NotFoundException;
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
public class JPAAppUserDetailsManager implements UserDetailsManager {
    private JPAAppUserDetailsRepository repository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final PasswordEncoder encoder;

    public JPAAppUserDetailsManager(JPAAppUserDetailsRepository repository, PasswordEncoder encoder) {
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
            throw new RuntimeException("User details implementation must be 'AppUserDetails'", e);
        }
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        try {
            var user = (AppUserDetails) userDetails;
            var userFromDatabase = loadUserByUsername(user.getUsername());

            if (userFromDatabase == null)
                throw new NotFoundException();

            if (!user.getPassword().equals(userFromDatabase.getPassword()))
                throw new IllegalArgumentException("Password can be changed only with changePassword method");

            repository.update(user);
        } catch (ClassCastException e) {
            throw new RuntimeException("User details implementation must be 'AppUserDetails'", e);
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
            throw new IllegalCallerException("UserDetails interface implementation should be 'AppUserDetails'");
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
    public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found."));
    }
}
