package hulio13.articlesApi.domain.entity;

import hulio13.articlesApi.exception.IncorrectStringLengthException;
import jakarta.persistence.*;
import lombok.*;

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

    @NonNull
    @Getter
    @Column(name = "title")
    private String title;

    @Column(name = "coverImgUrl")
    @Getter
    private String coverImgUrl;

    @Column(name = "creationTime")
    private LocalDateTime creationTime;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors = new ArrayList<Author>();

    public Article(long id, @NonNull String title) {
        this.id = id;
        this.title = title;
        this.creationTime = LocalDateTime.now();
    }
    public Article(long id, @NonNull String title, List<Author> authors){
        this(id, title);
        this.authors = authors;
    }

    public Article(long id, @NonNull String title, String coverImgUrl){
        this(id, title);
        this.coverImgUrl = coverImgUrl;
    }

    public Article(long id, @NonNull String title, String coverImgUrl, List<Author> authors){
        this(id, title, coverImgUrl);
        this.authors = authors;
    }

    public void setTitle(String title) {
        if (title.length() > 150){
            throw new IncorrectStringLengthException(
                    "String should have length less or equal 150");
        }
        this.title = title;
    }
}