package hulio13.articlesApi.domain.data.repository;

import hulio13.articlesApi.domain.entity.Article;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends Repository<Article> {
    List<Article> getAll(int pageNumber, int pageSize, String sortBy, boolean isDescending, boolean showHidden);
    Optional<Article> getByTitle(String title);
    List<Article> getAllByIds(List<Long> ids);
}
