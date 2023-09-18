package hulio13.articlesApi.domain.entity

import hulio13.articlesApi.domain.entity.author.AuthorName
import jakarta.persistence.*

@Entity
@Table(name = "authors")
open class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Embedded
    private var _name: AuthorName,
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    val articles: MutableList<Article> = mutableListOf(),
) {
    constructor(authorName: AuthorName) : this(_name = authorName) {

    }

    var name: AuthorName
        get() = _name
        set(value) {
            _name = value.copy()
        }
}