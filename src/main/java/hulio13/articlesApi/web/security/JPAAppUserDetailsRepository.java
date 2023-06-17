package hulio13.articlesApi.web.security;

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
        em.remove(appUserDetails);
    }

    public void update(AppUserDetails appUserDetails) {
        em.merge(appUserDetails);
    }

    public void create(AppUserDetails appUserDetails) {
        if (getByUsername(appUserDetails.getUsername()).isEmpty())
            em.persist(appUserDetails);
    }
}
