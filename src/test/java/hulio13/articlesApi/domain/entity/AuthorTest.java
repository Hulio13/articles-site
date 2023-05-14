package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.domain.entity.article.ArticleTitle;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.domain.exception.AddException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    private static final String AUTHOR_NAME = "Nikita Petrovich";

    Author testAuthor;

    @BeforeEach
    void setUp(){
        testAuthor = new Author(1, new AuthorName(AUTHOR_NAME));
    }

    @Test
    void addArticles() {
        testAuthor.addArticle(new Article(1, testAuthor, new ArticleTitle("Some article")));
        testAuthor.addArticle(new Article(2, testAuthor, new ArticleTitle("Some article2")));

        if (testAuthor.getArticleById(1).isEmpty())
            fail("Article was not added.");

        assertThrows(AddException.class,
                () -> testAuthor.addArticle(new Article(1, testAuthor, new ArticleTitle("Bad article"))));
    }

    @Test
    void getId() {
        testAuthor.addArticle(new Article(1, testAuthor, new ArticleTitle("Some article")));
        assertTrue(testAuthor.getArticleById(1).isPresent());
    }

    @Test
    void removeArticleById() {
        testAuthor.addArticle(new Article(1, testAuthor, new ArticleTitle("Some article")));

        if (testAuthor.removeArticleById(1)){
            assertTrue(testAuthor.getArticleById(1).isEmpty());
        }
        else{
            fail("Article has not been deleted.");
        }
    }

    @Test
    void getName() {
        Author author = new Author(1, new AuthorName(AUTHOR_NAME));
        assertEquals(AUTHOR_NAME, author.getName().Value);
    }

    @Test
    void setName() {
        Author author = new Author(1, new AuthorName("Some Name"));
        author.setName(new AuthorName(AUTHOR_NAME));
        assertEquals(AUTHOR_NAME, author.getName().Value);
    }

    @Test
    void getAllArticles(){
        testAuthor.addArticle(new Article(1, testAuthor, new ArticleTitle("Some article")));
        testAuthor.addArticle(new Article(2, testAuthor, new ArticleTitle("Some article2")));
        testAuthor.addArticle(new Article(3, testAuthor, new ArticleTitle("Some article3")));

        List<Article> articles = testAuthor.getAllArticles();

        assertEquals(1, articles.get(0).getId());
        assertEquals(3, articles.get(2).getId());
        assertEquals(3, articles.size());
    }

    @Test
    void attemptToCreateAuthorWithTooLongName(){
        String tooLongName = "N".repeat(AuthorName.MAX_LENGTH + 1);

        assertThrows(Exception.class,
                () -> new Author(1, new AuthorName(tooLongName)));
    }
}