package hulio13.articlesApi.application.service;

import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void add(Author author){
        repository.save(author);
    }


    public Optional<Author> getById(long id){
        return repository.getById(id);
    }
}
