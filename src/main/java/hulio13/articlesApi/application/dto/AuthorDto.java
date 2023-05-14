package hulio13.articlesApi.application.dto;

import hulio13.articlesApi.domain.entity.Author;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import lombok.Data;

@Data
public class AuthorDto {
    private long id;
    private String name;

    public static AuthorDto toDto(Author author){
        AuthorDto dto = new AuthorDto();
        dto.id = author.getId();
        dto.name = author.getName().Value;

        return dto;
    }

    public static Author toDomainObject(AuthorDto dto){
        return new Author(dto.id, new AuthorName(dto.name));
    }
}
