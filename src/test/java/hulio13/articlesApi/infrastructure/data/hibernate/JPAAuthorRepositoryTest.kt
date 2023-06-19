package hulio13.articlesApi.infrastructure.data.hibernate

import hulio13.articlesApi.api.exceptions.NotFoundException
import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.infrastructure.data.AlreadyExistException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.lang.IllegalArgumentException

const val FIRST_AUTHOR_NAME = "UniqueAuthorName"
const val NEW_AUTHOR_NAME = "NewName"

@DataJpaTest
@EnableJpaRepositories
@Import(JPAAuthorRepository::class)
internal open class JPAAuthorRepositoryTest(
    @Autowired val em: TestEntityManager,
    @Autowired val repository: JPAAuthorRepository
) {

    @Test
    fun getById_getNonexistentAuthor_emptyOptionalReturned() {
        assertTrue(repository.getById(-1).isEmpty)
    }

    @Test
    fun getById_getExistentAuthor_optionalWithAuthorReturned() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)
        val id = author.id
        em.detach(author)

        val returnedAuthor = repository.getById(id);

        assertThat(returnedAuthor).isPresent.hasValueSatisfying { it.name.Value.equals(FIRST_AUTHOR_NAME) }
    }

    @Test
    fun getAll_noAuthorsInDatabase_emptyListReturned() {
        assertTrue(repository.all.isEmpty())
    }

    @Test
    fun getAll_oneAuthorInDatabase_listWithOneAuthorReturned() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)

        assertTrue(repository.all.size == 1)
    }

    @Test
    fun getAll_withManyAuthorsInDatabase_notEmptyListReturned() {
        val authors = listOf<Author>(
            Author(AuthorName("SomeName1")), Author(AuthorName("SomeName2")),
            Author(AuthorName("SomeName3")), Author(AuthorName("SomeName4"))
        )

        authors.forEach { em.persist(it) }

        assertTrue(repository.all.isNotEmpty())
    }

    @Test
    fun remove_nonexistentAuthor_throwsNotFoundException() {
        assertThrows<NotFoundException> { repository.remove(Author(AuthorName(FIRST_AUTHOR_NAME))) }
    }

    @Test
    fun remove_existentUser_removedFromDatabase() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)
        val id = author.id

        repository.remove(author)

        assertNull(em.find(Author::class.java, id))
    }

    @Test
    fun removeById_nonexistentAuthor_throwsNotFoundException() {
        assertThrows<NotFoundException> { repository.removeById(-1) }
    }

    @Test
    fun removeById_existentUser_removedFromDatabase() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)
        val id = author.id

        repository.removeById(id)

        assertNull(em.find(Author::class.java, id))
    }

    @Test
    fun getAuthorByName_nonexistentAuthor_emptyOptional() {
        assertTrue(repository.getByAuthorName("Non existent name").isEmpty)
    }

    @Test
    fun getAuthorByName_existentAuthor_OptionalWithAuthor() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)

        val returnedAuthor = repository.getByAuthorName(FIRST_AUTHOR_NAME)

        assertTrue(returnedAuthor.isPresent)
        assertTrue(returnedAuthor.get().name.Value.equals(FIRST_AUTHOR_NAME))
    }

    @Test
    fun create_withExistingName_throwsAlreadyExistException() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)

        assertThrows<AlreadyExistException> { repository.create(Author(AuthorName(FIRST_AUTHOR_NAME))) }
    }

    @Test
    fun create_withId_throwsAlreadyExistException() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)

        assertThrows<AlreadyExistException> { repository.create(Author(author.id, AuthorName("SomeRandomName"))) }
    }

    @Test
    fun create_authorWithName_addedToDatabase() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        repository.create(author)

        val returnedAuthor = repository.getByAuthorName(FIRST_AUTHOR_NAME)

        assertTrue(
            returnedAuthor.isPresent &&
                    returnedAuthor.get().name.Value.equals(FIRST_AUTHOR_NAME)
        )
    }

    @Test
    fun create_nullInsteadOfAuthor_throwsIllegalArgumentException() {
        assertThrows<IllegalArgumentException> { repository.create(null) }
    }


    @Test

    fun update_nonexistentAuthor_throwsNotFoundException() {
        assertThrows<NotFoundException> { repository.update(Author(AuthorName("NonexistentAuthor"))) }
    }

    @Test
    fun update_withAuthor_updatedInDatabase() {
        val author = Author(AuthorName(FIRST_AUTHOR_NAME))

        em.persistAndFlush(author)

        author.name = AuthorName(NEW_AUTHOR_NAME)

        repository.update(author)

        val returnedAuthor = em.find(Author::class.java, author.id)

        assertTrue(returnedAuthor.name.Value.equals(NEW_AUTHOR_NAME))
    }
}