package it.unito.di.other;

public class SupplyException extends Throwable {
    public SupplyException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "[SupplyException] - " + getMessage();
    }

}
