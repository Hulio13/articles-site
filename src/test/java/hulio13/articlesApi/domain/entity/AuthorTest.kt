package hulio13.articlesApi.domain.entity

import hulio13.articlesApi.domain.entity.article.ArticleTitle
import hulio13.articlesApi.domain.entity.article.ARTICLES_MAX_LENGTH
import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.domain.exception.AddException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class AuthorTest {
    var testAuthor: Author? = null
    @BeforeEach
    fun setUp() {
        testAuthor = Author(1, AuthorName(AUTHOR_NAME))
    }

    @get:Test
    val name: Unit
        get() {
            val author = Author(1, AuthorName(AUTHOR_NAME))
            Assertions.assertEquals(AUTHOR_NAME, author.name.value)
        }

    @Test
    fun setName() {
        val author = Author(1, AuthorName("Some Name"))
        author.name = AuthorName(AUTHOR_NAME)
        Assertions.assertEquals(AUTHOR_NAME, author.name.value)
    }

    @get:Test
    val allArticles: Unit
        get() {
            testAuthor!!.articles.add(Article(1, authors = mutableListOf(testAuthor!!), title = ArticleTitle("Some article")))
            testAuthor!!.articles.add(Article(2, authors = mutableListOf(testAuthor!!), title = ArticleTitle("Some article")))
            testAuthor!!.articles.add(Article(3, authors = mutableListOf(testAuthor!!), title = ArticleTitle("Some article")))
            val articles: List<Article> = testAuthor!!.articles
            Assertions.assertEquals(1, articles[0].id)
            Assertions.assertEquals(3, articles[2].id)
            Assertions.assertEquals(3, articles.size)
        }

    @Test
    fun attemptToCreateAuthorWithTooLongName() {
        val tooLongName = "N".repeat(ARTICLES_MAX_LENGTH + 1)
        Assertions.assertThrows(
            Exception::class.java
        ) { Author(1, AuthorName(tooLongName)) }
    }

    companion object {
        private const val AUTHOR_NAME = "Nikita Petrovich"
    }
}