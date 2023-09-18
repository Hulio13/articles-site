package hulio13.articlesApi.application.dto

import hulio13.articlesApi.domain.entity.Article
import hulio13.articlesApi.domain.entity.Author

data class ArticleDto(
    var id: Long? = 0,
    var title: String? = null,
    var isHidden: Boolean? = false,
    var coverImgUrl: String? = null,
    var creationTime: String? = null,
    var markdownText: String? = null,
    var authorNames: MutableList<String> = ArrayList()
) {
    companion object {
        fun toDto(article: Article): ArticleDto {
            return ArticleDto(
                id = article.id,
                title = article.title.value,
                isHidden = article.isHidden,
                coverImgUrl = article.coverImgUrl,
                creationTime = article.creationTime?.toString(),
                markdownText = article.markdownText,
                authorNames = article.authors.map { a: Author -> a.name.value }.toMutableList()
            )
        }
    }
}
