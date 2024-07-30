package it.unito.di.press;

public class PlateException extends Throwable {
    public PlateException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "[PlateException] - " + getMessage();
    }
}
