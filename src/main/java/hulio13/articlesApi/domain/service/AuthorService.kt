package hulio13.articlesApi.domain.service

import hulio13.articlesApi.application.dto.AuthorDto
import hulio13.articlesApi.domain.data.repository.ArticleRepository
import hulio13.articlesApi.domain.data.repository.AuthorRepository
import hulio13.articlesApi.domain.entity.Article
import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.domain.entity.common.Result
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val articleRepository: ArticleRepository
    ) {
    fun add(dto: AuthorDto): Result<Author> {
        if (dto.name == null) return Result.error(1, "Invalid data, name must not be empty.")

        val author = Author(null, AuthorName(dto.name!!))

        return Result.ok(authorRepository.create(author))
    }

    fun update(author: Author) {
        authorRepository.update(author)
    }

    fun getById(id: Long): Optional<Author> {
        return authorRepository.getById(id)
    }

    fun getByAuthorName(authorName: String?): Optional<Author> {
        return authorRepository.getByAuthorName(authorName)
    }

    fun remove(entity: Author) {
        authorRepository.remove(entity)
    }

    fun removeById(id: Long) {
        authorRepository.removeById(id)
    }

    fun linkArticleToAuthor(author: Author, article: Article) {
        author.articles.add(article)
        article.authors.add(author)
        authorRepository.update(author)
    }
}
