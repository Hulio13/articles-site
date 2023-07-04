package hulio13.articlesApi.web.entities;

public record Result<T>(boolean isSuccess, T data, Error error) {
    public static <T> Result<T> ok(T entity) {
        return new Result<>(true, entity, null);
    }

    public static <T> Result<T> error(int errCode, String msg){
        return new Result<>(false, null, new Error(errCode, msg));
    }
}
