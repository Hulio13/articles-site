package hulio13.articlesApi.application.dto

import hulio13.articlesApi.domain.entity.Author
import hulio13.articlesApi.domain.entity.author.AuthorName
import lombok.Data

data class AuthorDto(
    var id: Long = 0,
    var name: String? = null,
    val articleIds: List<Long>? = null
) {


    companion object {
        @JvmStatic
        fun toDto(author: Author): AuthorDto =
            AuthorDto(
                id = author.id,
                name = author.name.Value,
                articleIds = author.allArticles.map { it.id }
            )

        fun toDomainObject(dto: AuthorDto): Author {
            return if (dto.id > -1) {
                Author(dto.id, AuthorName(dto.name))
            } else {
                Author(AuthorName(dto.name))
            }
        }
    }
}
