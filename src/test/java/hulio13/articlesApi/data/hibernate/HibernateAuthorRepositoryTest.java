package hulio13.articlesApi.data.hibernate;

import hulio13.articlesApi.infrastructure.data.hibernate.JPAAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JPAAuthorRepository.class)
class HibernateAuthorRepositoryTest {
    private static final long FIRST_AUTHOR_ID = 1L;

    @Autowired
    private JPAAuthorRepository repository;

    @Autowired
    private TestEntityManager em;


}