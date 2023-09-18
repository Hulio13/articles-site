package hulio13.articlesApi.domain.entity.author

import hulio13.articlesApi.domain.exception.IllegalStringLengthException
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

const val MAX_LENGTH = 50

@Embeddable
@AttributeOverride(name = "Value", column = Column(name = "name", nullable = false, unique = true))
data class AuthorName(val value: String) : Cloneable {
    init {
        if (value.length > MAX_LENGTH) {
            throw IllegalStringLengthException(
                "Name length should be less or equal than $MAX_LENGTH"
            )
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as AuthorName
        return value == that.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
