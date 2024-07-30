package it.unito.di.belt;

public class DepositBeltException extends Throwable {
    public DepositBeltException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "[DepositBeltException] - " + getMessage();
    }
}
