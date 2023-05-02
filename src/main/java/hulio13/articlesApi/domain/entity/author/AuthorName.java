package hulio13.articlesApi.domain.entity.author;

import hulio13.articlesApi.domain.exception.IllegalStringLengthException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AttributeOverride(name = "Value", column = @Column(name = "name", nullable = false, unique = true))
public class AuthorName {
    public static final int MAX_LENGTH = 50;

    public final String Value;

    public AuthorName(String value){
        if (value.length() > MAX_LENGTH){
            throw new IllegalStringLengthException(
                    "Name length should be less or equal than " + MAX_LENGTH);
        }

        Value = value;
    }
}
