package hulio13.articlesApi.web.security

import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.infrastructure.data.AlreadyExistException
import hulio13.articlesApi.web.exceptions.NotFoundException
import hulio13.articlesApi.web.security.entities.AppUserDetails
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import java.util.List
import java.util.function.Consumer

@DataJpaTest
@Import(JPAAppUserDetailsRepository::class)
internal class JPAAppUserDetailsRepositoryTestDto {
    @Autowired
    var em: TestEntityManager? = null

    @Autowired
    var repository: JPAAppUserDetailsRepository? = null

    @get:Test
    val byUsername_nonexistentUserDetails_emptyOptionalReturned: Unit
        get() {
            val result = repository!!.getByUsername(NON_EXISTENT_USERNAME)
            Assertions.assertTrue(result.isEmpty)
        }

    @get:Test
    val byUsername_userDetailsThatExist_optionalWithAuthorReturned: Unit
        get() {
            val details = AppUserDetails(
                FIRST_TEST_USERNAME, "somePass", true, true,
                false, true, author = Author(_name = AuthorName("someAuthor"))
            )
            em!!.persist(details)
            val result = repository!!.getByUsername(FIRST_TEST_USERNAME)
            Assertions.assertTrue(result.isPresent)
        }

    @get:Test
    val all_noElementsInDatabase_emptyListReturned: Unit
        get() {
            Assertions.assertTrue(repository!!.all.isEmpty())
        }

    @get:Test
    val all_withOneAppUserDetails_listWithUserDetailsReturned: Unit
        get() {
            val details = AppUserDetails(
                FIRST_TEST_USERNAME, "somePass",
                author = Author(AuthorName("someAuthor"))
            )
            em!!.persist(details)
            Assertions.assertEquals(1, repository!!.all.size)
        }

    @get:Test
    val all_withMoreThanOneAppUserDetails_listWithUserDetailsReturned: Unit
        get() {
            addThreeAuthorsToDatabase()
            Assertions.assertEquals(3, repository!!.all.size)
        }

    @Test
    fun remove_nonexistentUserDetails_throwsException() {
        val details = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        Assertions.assertThrows(NotFoundException::class.java) { repository!!.remove(details) }
    }

    @Test
    fun remove_existentUserDetails_removedFromDatabase() {
        val appUserDetails = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        em!!.persist(appUserDetails)
        em!!.flush()
        em!!.detach(appUserDetails)
        Assertions.assertNotNull(em!!.find(AppUserDetails::class.java, FIRST_TEST_USERNAME))
        repository!!.remove(appUserDetails)
    }

    @Test
    fun update_updateNonexistentUser_throwsException() {
        val appUserDetails = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        Assertions.assertThrows(NotFoundException::class.java) { repository!!.update(appUserDetails) }
    }

    @Test
    fun update_updateExistentUser_changedInDatabase() {
        val appUserDetails = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        em!!.persist(appUserDetails)
        em!!.flush()
        em!!.detach(appUserDetails)
        Assertions.assertNotNull(em!!.find(AppUserDetails::class.java, FIRST_TEST_USERNAME))
        appUserDetails.setLocked(true)
        appUserDetails.isEnabled = false
        repository!!.update(appUserDetails)
        val actualUserDetails = em!!.find(AppUserDetails::class.java, FIRST_TEST_USERNAME)
        Assertions.assertTrue(!actualUserDetails.isEnabled() && appUserDetails.isLocked() && actualUserDetails.getUsername() == FIRST_TEST_USERNAME)
    }

    @Test
    fun create_createUserDetailsThatAlreadyExist_throwsException() {
        val appUserDetails = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        em!!.persist(appUserDetails)
        em!!.flush()
        em!!.detach(appUserDetails)
        Assertions.assertNotNull(em!!.find(AppUserDetails::class.java, FIRST_TEST_USERNAME))
        val userDetailsWithSameUsername = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("existing user details"))
        )
        Assertions.assertThrows(AlreadyExistException::class.java) { repository!!.create(userDetailsWithSameUsername) }
    }

    @Test
    fun create_createNewUser_addedToDatabase() {
        val appUserDetails = AppUserDetails(
            FIRST_TEST_USERNAME, "somePass",
            author = Author(AuthorName("test"))
        )
        repository!!.create(appUserDetails)
        val userDetailsInDatabase = em!!.find(AppUserDetails::class.java, FIRST_TEST_USERNAME)
        Assertions.assertNotNull(userDetailsInDatabase)
    }

    private fun addThreeAuthorsToDatabase() {
        val details = List.of(
            AppUserDetails(FIRST_TEST_USERNAME, "somePass", author = Author(AuthorName("Author name"))),
            AppUserDetails(SECOND_TEST_USERNAME, "somePass", author = Author(AuthorName("Author name 1"))),
            AppUserDetails(THIRD_TEST_USERNAME, "somePass", author = Author(AuthorName("Author name 2")))
        )
        details.forEach(Consumer { userDetails: AppUserDetails -> em!!.persist(userDetails) })
        em!!.flush()
        details.forEach(Consumer { userDetails: AppUserDetails? -> em!!.detach(userDetails) })
    }

    companion object {
        private const val NON_EXISTENT_USERNAME = "NonExistentUsername"
        private const val FIRST_TEST_USERNAME = "TestUsername"
        private const val SECOND_TEST_USERNAME = "TestUsername2"
        private const val THIRD_TEST_USERNAME = "TestUsername3"
    }
}