package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.domain.entity.author.AuthorName;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "authors")
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Setter
    @Getter
    @NonNull
    @Embedded
    private AuthorName name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Article> articles = new ArrayList<>();

    public Author(long id, @NonNull String name) {
        this.id = id;
        this.name = new AuthorName(name);
    }

    public Author(long id, @NonNull String name, List<Article> articles){
        this(id, name);
        this.articles = articles;
    }
}
