package hulio13.articlesApi.web.security;

import hulio13.articlesApi.web.exceptions.NotFoundException;
import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.infrastructure.data.AlreadyExistException;
import hulio13.articlesApi.web.security.entities.AppUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(JPAAppUserDetailsRepository.class)
class JPAAppUserDetailsRepositoryTest {
    private static final String NON_EXISTENT_USERNAME = "NonExistentUsername";
    private static final String FIRST_TEST_USERNAME = "TestUsername";
    private static final String SECOND_TEST_USERNAME = "TestUsername2";
    private static final String THIRD_TEST_USERNAME = "TestUsername3";

    @Autowired
    TestEntityManager em;

    @Autowired
    JPAAppUserDetailsRepository repository;

    @Test
    public void getByUsername_nonexistentUserDetails_emptyOptionalReturned() {
        var result = repository.getByUsername(NON_EXISTENT_USERNAME);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getByUsername_userDetailsThatExist_optionalWithAuthorReturned() {
        AppUserDetails details = new AppUserDetails(FIRST_TEST_USERNAME, "somePass", true, true,
                false, new Author(new AuthorName("someAuthor")));

        em.persist(details);

        var result = repository.getByUsername(FIRST_TEST_USERNAME);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void getAll_noElementsInDatabase_emptyListReturned() {
        Assertions.assertTrue(repository.getAll().isEmpty());
    }

    @Test
    public void getAll_withOneAppUserDetails_listWithUserDetailsReturned() {
        var details = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("someAuthor")));

        em.persist(details);

        Assertions.assertEquals(1, repository.getAll().size());
    }

    @Test
    public void getAll_withMoreThanOneAppUserDetails_listWithUserDetailsReturned() {
        addThreeAuthorsToDatabase();

        Assertions.assertEquals(3, repository.getAll().size());
    }

    @Test
    public void remove_nonexistentUserDetails_throwsException() {
        var details = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        Assertions.assertThrows(NotFoundException.class, () -> repository.remove(details));
    }

    @Test
    public void remove_existentUserDetails_removedFromDatabase() {
        AppUserDetails appUserDetails = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        em.persist(appUserDetails);
        em.flush();
        em.detach(appUserDetails);
        Assertions.assertNotNull(em.find(AppUserDetails.class, FIRST_TEST_USERNAME));

        repository.remove(appUserDetails);
    }

    @Test
    public void update_updateNonexistentUser_throwsException() {
        var appUserDetails = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        Assertions.assertThrows(NotFoundException.class, () -> repository.update(appUserDetails));
    }

    @Test
    public void update_updateExistentUser_changedInDatabase() {
        var appUserDetails = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        em.persist(appUserDetails);
        em.flush();
        em.detach(appUserDetails);

        Assertions.assertNotNull(em.find(AppUserDetails.class, FIRST_TEST_USERNAME));

        appUserDetails.setLocked(true);
        appUserDetails.setEnabled(false);

        repository.update(appUserDetails);

        var actualUserDetails = em.find(AppUserDetails.class, FIRST_TEST_USERNAME);

        Assertions.assertTrue(!actualUserDetails.isEnabled() && appUserDetails.isLocked() &&
                actualUserDetails.getUsername().equals(FIRST_TEST_USERNAME));
    }

    @Test
    public void create_createUserDetailsThatAlreadyExist_throwsException() {
        var appUserDetails = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        em.persist(appUserDetails);
        em.flush();
        em.detach(appUserDetails);

        Assertions.assertNotNull(em.find(AppUserDetails.class, FIRST_TEST_USERNAME));

        var userDetailsWithSameUsername = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("existing user details")));

        Assertions.assertThrows(AlreadyExistException.class, () -> repository.create(userDetailsWithSameUsername));
    }

    @Test
    public void create_createNewUser_addedToDatabase() {
        var appUserDetails = new AppUserDetails(FIRST_TEST_USERNAME, "somePass",
                new Author(new AuthorName("test")));

        repository.create(appUserDetails);

        var userDetailsInDatabase = em.find(AppUserDetails.class, FIRST_TEST_USERNAME);

        Assertions.assertNotNull(userDetailsInDatabase);
    }

    private void addThreeAuthorsToDatabase() {
        var details = List.of(
                new AppUserDetails(FIRST_TEST_USERNAME, "somePass", new Author(new AuthorName("Author name"))),
                new AppUserDetails(SECOND_TEST_USERNAME, "somePass", new Author(new AuthorName("Author name 1"))),
                new AppUserDetails(THIRD_TEST_USERNAME, "somePass", new Author(new AuthorName("Author name 2")))
        );

        details.forEach(userDetails -> em.persist(userDetails));

        em.flush();

        details.forEach(userDetails -> em.detach(userDetails));
    }
}