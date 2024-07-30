package it.unito.di.elevatingrotarytable;

import it.unito.di.metalplate.MetalPlate;

import java.util.LinkedList;

public class ElevatingRotaryTable {
    private int positionY;
    private float angle;

    private boolean working;

    private MetalPlate  contains;

    public ElevatingRotaryTable() {
        this.positionY = 0;
        this.angle = 0;
        this.working = false;
        this.contains = null;
    }

    public void rotateTable(){
        setAngle(getAngle()+20);
        if(getAngle()==180){
            setAngle(0);
        }
    }
    public void transferTo(MetalPlate rawPlate){
        if(rawPlate.getState()==1){
            rawPlate.setState(2);
            this.contains= rawPlate;
        }
    }

    public void upTable() throws ElevatingRotaryTableException {
        if(getPositionY() == 10){
            throw new ElevatingRotaryTableException("Maximum height reached");
        }else{
            setPositionY(getPositionY()+2);
        }
    }

    public void downTable() throws ElevatingRotaryTableException {
        if(getPositionY() == 0){
            throw new ElevatingRotaryTableException("Minimum height reached");
        }else{
            setPositionY(getPositionY()-2);
        }
    }

    public void resetTable(){
        setAngle(0);
        setPositionY(0);
    }


    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public MetalPlate getContains() {
        return contains;
    }

    public void setContains(MetalPlate contains) {
        this.contains = contains;
    }

    public boolean isEmpty(){
        return getContains()==null;
    }

    @Override
    public String toString() {
        return "ELEVATING ROTARY TABLE{" +
                "positionY=" + positionY +
                ", angle=" + angle +
                ", working=" + working +
                ", contains=" + contains +
                '}';
    }
}
