package hulio13.articlesApi.domain.service;

import hulio13.articlesApi.domain.entity.Article;
import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.data.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public void add(Author author){
        repository.create(author);
    }

    public void update(Author author){
        repository.update(author);
    }

    public Optional<Author> getById(long id){
        return repository.getById(id);
    }

    public Optional<Author> getByAuthorName(String authorName){
        return repository.getByAuthorName(authorName);
    }

    public void remove(Author entity){
        repository.remove(entity);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }

    public void linkArticleToAuthor(Author author, Article article) {
        author.addArticle(article);
        article.addAuthor(author);
        repository.update(author);
    }
}
