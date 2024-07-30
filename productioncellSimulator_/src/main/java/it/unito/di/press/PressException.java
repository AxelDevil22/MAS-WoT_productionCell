package it.unito.di.press;

public class PressException extends Throwable {
        public PressException(String message) {
            super(message);
        }

    @Override
    public String toString() {
        return "[PressException] - " + getMessage();
    }
}
