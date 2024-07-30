package it.unito.di.belt;

import it.unito.di.metalplate.MetalPlate;


import java.util.LinkedList;


public class DepositBelt {

    private double speed;
    private boolean running;

    private int maxCapacity;
    private PhotoeletricCell equipped;

    private LinkedList<MetalPlate> carries;

    private boolean working;

    public DepositBelt() {
        this.speed = 0.2;
        this.running = false;
        this.equipped = new PhotoeletricCell();
        this.carries = new LinkedList<MetalPlate>();
        this.working = false;
        this.maxCapacity = 7;
    }





    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    public PhotoeletricCell getEquipped() {
        return equipped;
    }

    public void setEquipped(PhotoeletricCell equipped) {
        this.equipped = equipped;
    }

    public LinkedList<MetalPlate> getCarries() {
        return carries;
    }

    public void setCarries(LinkedList<MetalPlate> carries) {
        this.carries = carries;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getEmptySlot(){
        return getMaxCapacity()-getCarries().size();
    }

    public int getEmployedSlot(){
        return getCarries().size();
    }

    public void insertPlate(MetalPlate metalPlate) throws DepositBeltException {
        if(getCarries().size() < getMaxCapacity()) {
            metalPlate.setState(6);
            getCarries().addLast(metalPlate);
            if (getCarries().size() > 0) {

                getEquipped().setState(true);
            }
        }else{
            throw  new DepositBeltException("Max Capacity of DepositBelt");
        }

    }

    public MetalPlate removeMetalPlate() throws DepositBeltException {
        if(getCarries().size()>0){
            return getCarries().removeFirst();
        }else{
            throw new DepositBeltException("There is no MetalPlate in the Deposit Belt");
        }
    }

    public boolean isEmpty(){
        return getEmployedSlot()==0;
    }

    @Override
    public String toString() {
        return "DEPOSITBELT {" +
                "speed=" + speed +
                ", running=" + running +
                ", equipped=" + equipped +
                ", carries=" + carries +
                '}';
    }
}


