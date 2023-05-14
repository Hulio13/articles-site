package hulio13.articlesApi.application.dto;

import hulio13.articlesApi.domain.entity.Author;

public class AuthorDto {
    public long id;
    public String name;

    static public AuthorDto toDto(Author author){
        AuthorDto dto = new AuthorDto();
        dto.id = author.getId();
        dto.name = author.getName().Value;

        return dto;
    }
}
