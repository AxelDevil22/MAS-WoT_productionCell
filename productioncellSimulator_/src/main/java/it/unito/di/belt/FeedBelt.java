package it.unito.di.belt;


import java.util.LinkedList;
import it.unito.di.metalplate.MetalPlate;
public class FeedBelt {

    private double speed;
    private boolean running;

    private int maxCapacity;
    private PhotoeletricCell equipped;

    private LinkedList<MetalPlate> carries;

    private boolean working;
    public FeedBelt() {
        this.speed = 0.3;
        this.running = false;
        this.equipped = new PhotoeletricCell();
        this.carries = new LinkedList<MetalPlate>();
        this.working = false;
        this.maxCapacity = 5;
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
    public void addRawPlate(MetalPlate rawPlate) throws FeedBeltException {
        if(getCarries().size() < getMaxCapacity()){
            rawPlate.setState(1);
            carries.addLast(rawPlate);

        }else{
            throw  new FeedBeltException("Max Capacity of FeedBelt");
        }


    }
    public void removeRawPlate(MetalPlate rawPlate) throws FeedBeltException {
        if(getCarries().size() > 0){
            carries.remove(rawPlate);

        }else{
            throw  new FeedBeltException("FeedBelt is Empty");
        }


    }

    public boolean isEmpty(){
        return getEmployedSlot()==0;
    }


    @Override
    public String toString() {
        return "FEEDBELT {" +
                "speed=" + speed +
                ", running=" + running +
                ", maxCapacity=" + maxCapacity +
                ", equipped=" + equipped +
                ", carries=" + carries +
                ", working=" + working +
                '}';
    }
}
