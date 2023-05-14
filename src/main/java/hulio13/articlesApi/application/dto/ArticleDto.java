package hulio13.articlesApi.application.dto;

import hulio13.articlesApi.domain.entity.Article;
import hulio13.articlesApi.domain.entity.author.AuthorName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ArticleDto {
    private long id;

    private String title;

    private boolean isHidden;

    private String coverImgUrl;

    private LocalDateTime creationTime;

    private String markdownText;

    private List<String> authorNames = new ArrayList<>();

    public ArticleDto() {
    }

    public ArticleDto(long id, String title, boolean isHidden, String coverImgUrl,
                      LocalDateTime creationTime, String markdownText, List<String> authorNames) {
        this.id = id;
        this.title = title;
        this.isHidden = isHidden;
        this.coverImgUrl = coverImgUrl;
        this.creationTime = creationTime;
        this.markdownText = markdownText;
        this.authorNames.addAll(authorNames);
    }

    public static ArticleDto toDto(Article article){
        return new ArticleDto(article.getId(), article.getTitle().Value,
                article.isHidden(), article.getCoverImgUrl(), article.getCreationTime(),
                article.getMarkdownText(),
                article.getAuthors().stream().map(a -> a.getName().Value).collect(Collectors.toList()));
    }
}
