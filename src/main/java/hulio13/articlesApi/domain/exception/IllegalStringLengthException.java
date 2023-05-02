package hulio13.articlesApi.domain.exception;

public class IllegalStringLengthException extends RuntimeException{
    public IllegalStringLengthException() {
    }

    public IllegalStringLengthException(String message) {
        super(message);
    }
}
