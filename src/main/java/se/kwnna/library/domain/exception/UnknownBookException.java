package se.kwnna.library.domain.exception;

public class UnknownBookException extends RuntimeException {
    public UnknownBookException(String message) {
        super(message);
    }
}
