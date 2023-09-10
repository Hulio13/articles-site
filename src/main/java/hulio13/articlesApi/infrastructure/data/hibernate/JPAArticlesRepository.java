package hulio13.articlesApi.infrastructure.data.hibernate;

import hulio13.articlesApi.domain.entity.Article;
import hulio13.articlesApi.domain.data.repository.ArticleRepository;
import hulio13.articlesApi.infrastructure.data.AlreadyExistException;
import hulio13.articlesApi.web.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
    
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JPAArticlesRepository implements ArticleRepository {
    private static final List<String> sortByOptions = Arrays.asList("id", "title", "creation_time");

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Article> getById(long id) {
        return Optional.ofNullable(em.find(Article.class, id));
    }

    @Override
    public List<Article> getAll() {
        return em.createQuery("select a from Article a", Article.class).getResultList();
    }

    @Override
    public List<Article> getAll(int pageNumber, int pageSize, String sortBy, boolean isDescending, boolean showHidden) {
        if (!sortByOptions.contains(sortBy))
            throw new IllegalArgumentException(sortBy + " is not possible option to sort.");

        return em.createQuery("select a from Article a " + (showHidden ? "" : "where a.isHidden == false ") +
                        "order by :sortBy" + (isDescending ? " DESC" : "") +
                        " limit " + pageSize + " offset " + pageSize*pageNumber, Article.class)
                .setParameter("sortBy", sortBy)
                .getResultList();
    }

    @Override
    public Optional<Article> getByTitle(String title) {
        return em.createQuery("select a from Article a where a.title = :title", Article.class)
                .setParameter("title", title)
                .getResultStream().findFirst();
    }

    @Override
    public List<String> getPossibleSortOptions() {
        return sortByOptions;
    }

    @Override
    public void remove(Article entity) {
        var article = getById(entity.getId());
        if (article.isEmpty())
            throw new NotFoundException("Article with id '" + entity.getId() +
                    "' not found");

        em.remove(article.get());
    }

    @Override
    public void removeById(long id) {
        var article = getById(id).orElseThrow(() -> new NotFoundException("Article with id '" + id +
                "' not found"));

        em.remove(article);
    }

    @Override
    public Article create(Article entity) {
        if (entity == null) throw new IllegalArgumentException("Entity is null, Article expected.");

        if (getById(entity.getId()).isPresent())
            throw new AlreadyExistException("Article with id '" + entity.getId() + "' already exist.");

        Optional<Article> byArticleTitle = getByTitle(entity.getTitle().Value);
        if (byArticleTitle.isPresent())
            throw new AlreadyExistException("Article with title '" + entity.getTitle().Value +
                    "' already exist.");

        em.persist(entity);
        return entity;
    }

    @Override
    public Article update(Article entity) {
        var article = getById(entity.getId())
                .orElseThrow(() -> new NotFoundException("Article with id '" + entity.getId() + "' not found"));

        article.setTitle(entity.getTitle());
        if (entity.getIsHidden() != null)
            article.setIsHidden(entity.getIsHidden());
        article.setCoverImgUrl(entity.getCoverImgUrl());
        article.getAuthors().addAll(entity.getAuthors());
        article.setMarkdownText(entity.getMarkdownText());

        return em.merge(article);
    }
}
