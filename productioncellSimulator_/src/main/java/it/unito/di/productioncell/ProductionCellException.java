package it.unito.di.productioncell;

public class ProductionCellException extends Throwable {
    public ProductionCellException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "[ProductionCellException] - " + getMessage();
    }

}
