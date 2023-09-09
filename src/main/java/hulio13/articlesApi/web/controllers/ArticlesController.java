package hulio13.articlesApi.web.controllers;

import hulio13.articlesApi.application.dto.ArticleDto;
import hulio13.articlesApi.domain.service.ArticleService;
import hulio13.articlesApi.domain.service.AuthorService;
import hulio13.articlesApi.domain.entity.Article;
import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.entity.article.ArticleTitle;
import hulio13.articlesApi.domain.exception.IllegalStringLengthException;
import hulio13.articlesApi.infrastructure.data.AlreadyExistException;
import hulio13.articlesApi.web.entities.Result;
import hulio13.articlesApi.web.security.entities.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/article/")
public class ArticlesController {
    public final ArticleService articleService;

    public final AuthorService authorService;

    public ArticlesController(ArticleService articleService, AuthorService authorService) {
        this.articleService = articleService;
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<Result<List<Article>>> getAllWithSorting(Authentication authentication,
                                                           @RequestParam(defaultValue = "0") int pageNumber,
                                                           @RequestParam(defaultValue = "10") int pageSize,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "true") boolean isDescending,
                                                           @RequestParam(defaultValue = "false") boolean showHidden) {
        AppUserDetails details = (AppUserDetails) authentication.getPrincipal();

        if (showHidden && details.getRoles().stream().noneMatch(d -> d.getRole().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                Result.ok(
                        articleService.getAllWithPaginationAndSorting(pageNumber, pageSize, sortBy, isDescending, showHidden)
                )
        );
    }

    @PostMapping("{title}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<Result<ArticleDto>> createArticle(Authentication authentication, @PathVariable String title) {
        ArticleTitle articleTitle;

        Author author = ((AppUserDetails) authentication.getPrincipal()).getAuthor();

        try{
            articleTitle = new ArticleTitle(title);
        } catch (IllegalStringLengthException e) {
            return ResponseEntity.badRequest().body(Result.error(1, e.getMessage()));
        }

        var article = new Article(articleTitle);

        try{
            article = articleService.create(article);
        } catch (AlreadyExistException e){
            return ResponseEntity.badRequest().body(Result.error(100, e.getMessage()));
        }

        authorService.linkArticleToAuthor(author, article);

        return ResponseEntity.ok(Result.ok(ArticleDto.toDto(article)));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<Result<Void>> editArticle(Authentication authentication,
                                                    @RequestBody ArticleDto articleDto) {
        if (articleDto.getId() == null) {
            return ResponseEntity.ok(Result.error(
                    2, "Must contain next data: id (long)"
            ));
        }

        if (articleDto.getTitle() == null) {
            return ResponseEntity.ok(Result.error(
                    2, "Must contain next data: title (long)"
            ));
        }

        var articleOptional = articleService.getById(articleDto.getId());
        if (articleOptional.isEmpty()) {
            return ResponseEntity.ok(Result.error(
                    101, "Article with id " + articleDto.getId() + " not found."
            ));
        }

        var article = articleOptional.get();

        AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
        var author = details.getAuthor();

        if (article.getAuthors().stream().filter((it) -> it.getId() == author.getId()).findAny().isEmpty()) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(Result.ok(null));
    }
}
