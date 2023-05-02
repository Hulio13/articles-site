package hulio13.articlesApi.domain.repository;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T> {
    T getById(long id);

    List<T> getAll();

    List<T> getAllIf(Predicate<T> predicate);

    void add(T entity);

    void remove(T entity);

    void removeById(long id);

    T save(T entity);
}
