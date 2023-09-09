package hulio13.articlesApi.web.controllers;

import hulio13.articlesApi.application.dto.AuthorDto;
import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.web.entities.Result;
import hulio13.articlesApi.web.security.entities.AppUserDetails;
import hulio13.articlesApi.web.exceptions.NotFoundException;
import hulio13.articlesApi.domain.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public Result<AuthorDto> getById(@PathVariable long id){
        return Result.ok(authorService
                .getById(id)
                .map(AuthorDto::toDto)
                .orElseThrow(NotFoundException::new));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('AUTHOR')")
    public Result<AuthorDto> getInfo(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Result.ok(AuthorDto.toDto(((AppUserDetails)userDetails).getAuthor()));
    }

    @PutMapping("/changeUsername/{newUsername}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<Result<Boolean>> changeUsername(Authentication authentication, @PathVariable String newUsername){
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

        Author author = authorService.getById(appUserDetails.getAuthor().getId()).orElseThrow();

        author.setName(new AuthorName(newUsername));

        authorService.update(author);

        return ResponseEntity.ok(Result.ok(true));
    }


    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result<Boolean>> createAuthor(@RequestBody AuthorDto author){
        authorService.add(AuthorDto.Companion.toDomainObject(author));
        return ResponseEntity.ok().body(Result.ok(true));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result<String>> handleNotFound(NotFoundException exception) {
        return ResponseEntity.badRequest().body(Result.error(101, exception.getMessage()));
    }
}
