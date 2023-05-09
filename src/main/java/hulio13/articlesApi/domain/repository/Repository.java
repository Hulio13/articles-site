package hulio13.articlesApi.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T> {
    Optional<T> getById(long id);

    List<T> getAll();

    void add(T entity);

    void remove(T entity);

    void removeById(long id);

    T save(T entity);
}
