package it.unito.di.robot;

public class Arm {

    private int positionX;
    private int positionY;

    private Electromagnet equipped;

    public Arm() {
        this.positionX = positionX;
        this.positionY = positionY;
        this.equipped = new Electromagnet();
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Electromagnet getEquipped() {
        return equipped;
    }

    public void setEquipped(Electromagnet equipped) {
        this.equipped = equipped;
    }

    @Override
    public String toString() {
        return "ARM {" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", equipped=" + equipped +
                '}';
    }



}
