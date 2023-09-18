package hulio13.articlesApi.domain.service

import hulio13.articlesApi.application.dto.ArticleDto
import hulio13.articlesApi.domain.data.repository.ArticleRepository
import hulio13.articlesApi.domain.entity.Article
import hulio13.articlesApi.domain.entity.article.ArticleTitle
import hulio13.articlesApi.domain.entity.common.Result
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class ArticleService(private val repository: ArticleRepository) {
    fun getById(id: Long): Optional<Article> {
        return repository.getById(id)
    }

    val all: List<Any>
        get() = repository.getAll()

    fun getAllWithPaginationAndSorting(
        pageNumber: Int, pageSize: Int, sortBy: String?,
        isDescending: Boolean, showHidden: Boolean
    ): List<Article> {
        return repository.getAll(pageNumber, pageSize, sortBy, isDescending, showHidden)
    }

    fun getByTitle(title: String?): Optional<Article> {
        return repository.getByTitle(title)
    }

    val possibleSortOptions: List<String>
        get() = repository.getPossibleSortOptions()

    fun remove(article: Article) {
        repository.remove(article)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun create(article: Article): Article {
        return repository.create(article)
    }

    fun update(article: Article): Article {
        return repository.update(article)
    }

    fun updateFromDto(dto: ArticleDto): Result<Article> {
        if (dto.title == null) {
            return Result.error(2, "Title is not provided in dto.")
        }

        val article = Article(title = ArticleTitle(dto.title.toString()))
        if (dto.markdownText != null) {
            article.markdownText = dto.markdownText.toString()
        }
        if (dto.coverImgUrl != null) {
            article.coverImgUrl = dto.coverImgUrl
        }
        return Result.ok(repository.update(article))
    }
}
