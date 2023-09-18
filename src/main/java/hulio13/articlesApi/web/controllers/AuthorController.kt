package hulio13.articlesApi.web.controllers

import hulio13.articlesApi.application.dto.AuthorDto
import hulio13.articlesApi.application.dto.AuthorDto.Companion.toDto
import hulio13.articlesApi.application.dto.toDto
import hulio13.articlesApi.domain.entity.Article
import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.domain.entity.common.Result
import hulio13.articlesApi.domain.service.AuthorService
import hulio13.articlesApi.web.exceptions.NotFoundException
import hulio13.articlesApi.web.security.entities.AppUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.function.Supplier

@RestController
@RequestMapping("/api/author")
class AuthorController(private val authorService: AuthorService) {
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    fun getById(@PathVariable id: Long): Result<AuthorDto> {
        return Result.ok(
            authorService
                .getById(id)
                .map { it.toDto() }
                .orElseThrow { NotFoundException() }
        )
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('AUTHOR')")
    fun getInfo(authentication: Authentication): Result<AuthorDto> {
        val userDetails = authentication.principal as UserDetails
        return Result.ok(toDto((userDetails as AppUserDetails).author))
    }

    @PutMapping("/changeUsername/{newUsername}")
    @PreAuthorize("hasRole('AUTHOR')")
    fun changeUsername(
        authentication: Authentication,
        @PathVariable newUsername: String?
    ): ResponseEntity<Result<Boolean>> {
        val appUserDetails = authentication.principal as AppUserDetails
        val author = authorService.getById(appUserDetails.author.id!!).orElseThrow()
        author.name = AuthorName(newUsername!!)
        authorService.update(author)
        return ResponseEntity.ok(Result.ok(true))
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    fun createAuthor(@RequestBody author: AuthorDto): ResponseEntity<Result<Boolean>> {
        authorService.add(author)
        return ResponseEntity.ok().body(Result.ok(true))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<Result<Article>> {
        return ResponseEntity.badRequest().body(Result.error<Article>(101, exception.message))
    }
}
