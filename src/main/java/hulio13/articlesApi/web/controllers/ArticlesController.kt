package hulio13.articlesApi.web.controllers

import hulio13.articlesApi.application.dto.ArticleDto
import hulio13.articlesApi.domain.entity.Article
import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.article.ArticleTitle
import hulio13.articlesApi.domain.entity.common.Result
import hulio13.articlesApi.domain.exception.IllegalStringLengthException
import hulio13.articlesApi.domain.service.ArticleService
import hulio13.articlesApi.domain.service.AuthorService
import hulio13.articlesApi.infrastructure.data.AlreadyExistException
import hulio13.articlesApi.web.security.entities.AppUserDetails
import hulio13.articlesApi.web.security.entities.Role
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/article/")
class ArticlesController(val articleService: ArticleService, val authorService: AuthorService) {
    @GetMapping
    fun getAllWithSorting(
        authentication: Authentication,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "id") sortBy: String?,
        @RequestParam(defaultValue = "true") isDescending: Boolean,
        @RequestParam(defaultValue = "false") showHidden: Boolean
    ): ResponseEntity<Result<List<Article>>> {
        val details = authentication.principal as AppUserDetails
        return if (showHidden && details.getRoles().stream().noneMatch { d: Role -> d.role == "ROLE_ADMIN" }) {
            ResponseEntity.status(401).build()
        } else ResponseEntity.ok(
            Result.ok(
                articleService.getAllWithPaginationAndSorting(pageNumber, pageSize, sortBy, isDescending, showHidden)
            )
        )
    }

    @PostMapping("{title}")
    @PreAuthorize("hasRole('AUTHOR')")
    fun createArticle(
        authentication: Authentication,
        @PathVariable title: String
    ): ResponseEntity<Result<ArticleDto>> {
        val articleTitle: ArticleTitle
        val author = (authentication.principal as AppUserDetails).author
        try {
            articleTitle = ArticleTitle(title)
        } catch (e: IllegalStringLengthException) {
            return ResponseEntity.badRequest().body(Result.error(1, e.message))
        }
        var article = Article(title = articleTitle)
        article = try {
            articleService.create(article)
        } catch (e: AlreadyExistException) {
            return ResponseEntity.badRequest().body(Result.error(100, e.message))
        }
        authorService.linkArticleToAuthor(author, article)
        return ResponseEntity.ok(Result.ok(ArticleDto.toDto(article)))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    fun editArticle(
        authentication: Authentication,
        @RequestBody articleDto: ArticleDto
    ): ResponseEntity<Result<Unit>?> {
        if (articleDto.id == null) {
            return ResponseEntity.ok(
                Result.error(
                    2, "Must contain next data: id (long)"
                )
            )
        }
        if (articleDto.title == null) {
            return ResponseEntity.ok(
                Result.error(
                    2, "Must contain next data: title (long)"
                )
            )
        }
        val articleOptional = articleService.getById(articleDto.id!!)
        if (articleOptional.isEmpty) {
            return ResponseEntity.ok(
                Result.error(
                    101, "Article with id " + articleDto.id + " not found."
                )
            )
        }
        val article = articleOptional.get()
        val details = authentication.principal as AppUserDetails
        val author = details.author
        return if (article.authors.stream().filter { it: Author -> it.id === author.id }.findAny().isEmpty) {
            ResponseEntity.status(401).body(null)
        } else ResponseEntity.ok(Result.ok(Unit))
    }
}
