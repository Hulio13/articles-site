package hulio13.articlesApi.domain.exception;

public class AddException extends RuntimeException{
    public AddException() {
    }

    public AddException(String message) {
        super(message);
    }
}
