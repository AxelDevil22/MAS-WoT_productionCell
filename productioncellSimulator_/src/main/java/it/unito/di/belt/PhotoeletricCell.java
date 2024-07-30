package it.unito.di.belt;

public class PhotoeletricCell {

    private boolean state;

    public PhotoeletricCell() {
        this.state = false;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PhotoeletricCell{" +
                "state=" + state +
                '}';
    }
}
