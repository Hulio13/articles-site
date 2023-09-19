package hulio13.articlesApi.domain.entity.article

import hulio13.articlesApi.domain.exception.IllegalStringLengthException
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

const val ARTICLES_MAX_LENGTH = 150

@Embeddable
@AttributeOverride(name = "Value", column = Column(name = "title", nullable = false))
data class ArticleTitle(val value: String) : Cloneable {
    init {
        if (value.length > ARTICLES_MAX_LENGTH) {
            throw IllegalStringLengthException(
                "Name length should be less or equal than $ARTICLES_MAX_LENGTH"
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleTitle

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }


}