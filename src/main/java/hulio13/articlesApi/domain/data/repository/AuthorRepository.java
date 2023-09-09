package hulio13.articlesApi.domain.data.repository;

import hulio13.articlesApi.domain.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends Repository<Author> {
    Optional<Author> getByAuthorName(String name);

    List<Author> getAll(int pageNumber, int pageSize, String sortBy, boolean isDescending);
}
