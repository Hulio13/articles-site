package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.domain.exception.AddException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public void addArticle(Article article){
        if (articles.stream().anyMatch(a -> a.getId() == article.getId()))
            throw new AddException("Article with id '" + article.getId() +
                    "' already exist.");

        articles.add(article);
    }

    public boolean removeArticleById(long id){
        return articles.removeIf(a -> a.getId() == id);
    }

    public Optional<Article> getArticleById(long id){
        return articles.stream().filter(a -> a.getId() == id).findFirst();
    }

    public List<Article> getAllArticles(){
        return articles;
    }
}
