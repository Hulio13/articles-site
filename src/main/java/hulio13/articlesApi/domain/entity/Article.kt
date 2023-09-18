package hulio13.articlesApi.domain.entity

import hulio13.articlesApi.domain.entity.article.ArticleTitle
import hulio13.articlesApi.domain.utils.UrlValidator
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "articles")
open class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Embedded
    var title: ArticleTitle,

    @Column(name = "is_hidden")
    var isHidden: Boolean? = null,

    @Column(name = "creation_time", nullable = false)
    var creationTime: LocalDateTime? = null,

    @Column(name = "markdown_text")
    var markdownText: String = "",

    @Column(name = "cover_image_url")
    private var _coverImgUrl: String? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    var authors: MutableList<Author> = mutableListOf()
) {
    var coverImgUrl
        get() = _coverImgUrl
        set(value) {
            require(UrlValidator.IsUrlValid(coverImgUrl)) { "Value is not an url" }

            _coverImgUrl = value
        }
}