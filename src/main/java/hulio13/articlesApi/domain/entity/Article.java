package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.domain.entity.article.ArticleTitle;
import hulio13.articlesApi.domain.utils.UrlValidator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @NonNull @Getter @Setter @Embedded
    private ArticleTitle title;

    @Column(name = "cover_image_url")
    @Getter
    private String coverImgUrl;

    @Column(name = "creation_time", nullable = false)
    @Getter
    private LocalDateTime creationTime;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Author> authors = new ArrayList<>();

    private Article(long id, @NonNull String title){
        this.id = id;
        this.title = new ArticleTitle(title);
    }

    public Article(long id, Author author, @NonNull String title) {
        this(id, title);
        this.creationTime = LocalDateTime.now();
        authors.add(author);
    }
    public Article(long id, @NonNull String title, @NonNull List<Author> authors){
        this(id, title);
        this.authors = authors;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        if (!UrlValidator.IsUrlValid(coverImgUrl))
            throw new IllegalArgumentException("Value is not an url");

        this.coverImgUrl = coverImgUrl;
    }

    public void addAuthor(Author author){
        authors.add(author);
    }

    public boolean removeAuthorById(long id){
        return authors.removeIf(a -> a.getId() == id);
    }

    public List<Author> getAuthors(){
        return authors;
    }
}