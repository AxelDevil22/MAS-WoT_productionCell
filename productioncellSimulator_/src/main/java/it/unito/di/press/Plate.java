package it.unito.di.press;


public class Plate {

    private int positionX;

    public Plate() {
        this.positionX = 0;
    }
    public Plate(int positionX) {
        this.positionX = 0;
    }


    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void upPlate() throws PlateException {
        if(getPositionX() == 0){

            throw new PlateException("Maximum height reached");

        }else{
            setPositionX(getPositionX()+1);
        }

    }

    public void downPlate() throws PlateException {

        if(getPositionX() == -3){

            throw new PlateException("Minimum height reached");

        }else{
            setPositionX(getPositionX()-1);
        }

    }

    @Override
    public String toString() {
        return "Plate{" +
                "positionX=" + positionX +
                '}';
    }
}
