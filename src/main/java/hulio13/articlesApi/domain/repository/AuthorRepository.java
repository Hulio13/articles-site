package hulio13.articlesApi.domain.repository;

import hulio13.articlesApi.domain.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends Repository<Author> {
    Optional<Author> getByAuthorName(String name);
}
