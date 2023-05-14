package hulio13.articlesApi.infrastructure.data.hibernate;

import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }

    @Override
    public void remove(Author entity) {
        em.remove(entity);
    }

    @Override
    public void removeById(long id) {
        getById(id).ifPresent(a -> em.remove(a));
    }

    @Override
    public Author save(Author entity) {
        em.persist(entity);
        return entity;
    }
}
