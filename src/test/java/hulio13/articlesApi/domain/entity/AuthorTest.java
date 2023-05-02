package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.domain.exception.AddException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    private static final String AUTHOR_NAME = "Nikita Petrovich";

    Author testAuthor;

    @BeforeEach
    void setUp(){
        testAuthor = new Author(1, AUTHOR_NAME);
    }

    @Test
    void addArticles() {
        testAuthor.addArticle(new Article(1, "Some article"));
        testAuthor.addArticle(new Article(2, "Some article2"));

        if (testAuthor.getArticleById(1).isEmpty())
            fail("Article was not added.");

        assertThrows(AddException.class,
                () -> testAuthor.addArticle(new Article(1, "Bad article")));
    }

    @Test
    void getId() {
        testAuthor.addArticle(new Article(1, "Some article"));
        assertTrue(testAuthor.getArticleById(1).isPresent());
    }

    @Test
    void removeArticleById() {
        testAuthor.addArticle(new Article(1, "Some article"));

        if (testAuthor.removeArticleById(1)){
            assertTrue(testAuthor.getArticleById(1).isEmpty());
        }
        else{
            fail("Article has not been deleted.");
        }
    }

    @Test
    void getName() {
        Author author = new Author(1, AUTHOR_NAME);
        assertEquals(AUTHOR_NAME, author.getName().Value);
    }

    @Test
    void setName() {
        Author author = new Author(1, "Some Name");
        author.setName(new AuthorName(AUTHOR_NAME));
        assertEquals(AUTHOR_NAME, author.getName().Value);
    }
}