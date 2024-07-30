package it.unito.di.robot;

import it.unito.di.metalplate.MetalPlate;

public class Electromagnet {

    private boolean working;
    private boolean empty;

    private MetalPlate attract;

    public Electromagnet() {
        this.working = false;
        this.empty = true;
        this.attract = null;

    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public MetalPlate getAttract() {
        return attract;
    }

    public void setAttract(MetalPlate attract) {
        this.attract = attract;
    }

    @Override
    public String toString() {
        return "Electromagnet{" +
                "working=" + working +
                ", empty=" + empty +
                ", attract=" + attract +
                '}';
    }
}
