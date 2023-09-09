package hulio13.articlesApi.domain.data.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    Optional<T> getById(long id);

    List<T> getAll();

    List<String> getPossibleSortOptions();

    void remove(T entity);

    void removeById(long id);

    T create(T entity);

    T update(T entity);
}
