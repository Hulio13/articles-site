package hulio13.articlesApi.api.controllers;

import hulio13.articlesApi.api.exceptions.NotFoundException;
import hulio13.articlesApi.application.dto.AuthorDto;
import hulio13.articlesApi.application.service.AuthorService;
import hulio13.articlesApi.domain.entity.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public AuthorDto getById(@PathVariable long id){
        return authorService
                .getById(id)
                .map(AuthorDto::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping("/")
    public ResponseEntity<String> createAuthor(@RequestBody Author author){
        authorService.add(author);
        return ResponseEntity.ok().body("Ok");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        return ResponseEntity.badRequest().body("Not found");
    }
}
