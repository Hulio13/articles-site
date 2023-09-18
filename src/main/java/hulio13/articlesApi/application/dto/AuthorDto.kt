package hulio13.articlesApi.application.dto

import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName

data class AuthorDto(
    var id: Long? = 0,
    var name: String? = null,
    val articleIds: List<Long>? = null
) {
    companion object {
        fun toDto(author: Author): AuthorDto =
            AuthorDto(
                id = author.id,
                name = author.name.value,
                articleIds = author.articles.map { it.id ?: -1  }
            )
    }
}

fun Author.toDto() = AuthorDto.toDto(this)
