package hulio13.articlesApi.web.entities;

public record Result<T>(boolean isSuccess, T data, Error error) {
}
