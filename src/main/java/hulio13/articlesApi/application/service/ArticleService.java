package hulio13.articlesApi.application.service;

import hulio13.articlesApi.domain.entity.Article;
import hulio13.articlesApi.infrastructure.data.hibernate.JPAArticlesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleService {
    private final JPAArticlesRepository repository;


    public ArticleService(JPAArticlesRepository repository) {
        this.repository = repository;
    }

    public Optional<Article> getById(long id) {
        return repository.getById(id);
    }

    public List<Article> getAll(){
        return repository.getAll();
    }

    public List<Article> getAllWithPaginationAndSorting(int pageNumber, int pageSize, String sortBy,
                                                        boolean isDescending, boolean showHidden) {
        return repository.getAll(pageNumber, pageSize, sortBy, isDescending, showHidden);
    }

    public Optional<Article> getByTitle(String title){
        return repository.getByTitle(title);
    }

    public List<String> getPossibleSortOptions() {
        return repository.getPossibleSortOptions();
    }

    public void remove(Article article) {
        repository.remove(article);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }

    public Article create(Article article) {
        return repository.create(article);
    }

    public Article update(Article article) {
        return repository.update(article);
    }
}
