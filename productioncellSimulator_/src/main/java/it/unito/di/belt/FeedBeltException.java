package it.unito.di.belt;

public class FeedBeltException extends Throwable {
    public FeedBeltException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "[FeedBeltException] - " + getMessage();
    }
}
