package hulio13.articlesApi.web.security;

import hulio13.articlesApi.web.exceptions.NotFoundException;
import hulio13.articlesApi.infrastructure.data.AlreadyExistException;
import hulio13.articlesApi.web.security.entities.AppUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JPAAppUserDetailsRepository {
    @PersistenceContext
    private EntityManager em;

    public Optional<AppUserDetails> getByUsername(String username) {
        return Optional.ofNullable(em.find(AppUserDetails.class, username));
    }

    public List<AppUserDetails> getAll() {
        return em.createQuery("select u from AppUserDetails u", AppUserDetails.class).getResultList();
    }

    public void remove(AppUserDetails appUserDetails) {
        Optional<AppUserDetails> foundByUsername = getByUsername(appUserDetails.getUsername());
        if (foundByUsername.isEmpty())
            throw new NotFoundException("UserDetails with username '" + appUserDetails.getUsername()
                    + "' not found.");

        em.remove(foundByUsername.get());
    }

    public void update(AppUserDetails appUserDetails) {
        var foundByUsername = getByUsername(appUserDetails.getUsername());
        if (foundByUsername.isEmpty())
            throw new NotFoundException("UserDetails with username '" + appUserDetails.getUsername()
            + "' not found.");

        em.merge(appUserDetails);
    }

    public void create(AppUserDetails appUserDetails) {
        if (getByUsername(appUserDetails.getUsername()).isEmpty())
            em.persist(appUserDetails);
        else
            throw new AlreadyExistException("UserDetails already exist.");
    }
}
