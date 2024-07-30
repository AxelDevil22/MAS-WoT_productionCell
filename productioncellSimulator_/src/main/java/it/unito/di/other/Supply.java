package it.unito.di.other;

import it.unito.di.metalplate.MetalPlate;
import it.unito.di.press.PressException;

import java.util.LinkedList;

public class Supply {

    private LinkedList<MetalPlate> provides;

    public Supply() {
        this.provides = new LinkedList<MetalPlate>();
    }

    public LinkedList<MetalPlate> getProvides() {
        return provides;
    }

    public void setProvides(LinkedList<MetalPlate> provides) {
        this.provides = provides;
    }

    public void addRawPlate(MetalPlate rawPlate){
        provides.addLast(rawPlate);
    }

    public MetalPlate removeRawPlate() throws SupplyException {
        if(getProvides().size()>0){
            return getProvides().removeFirst();
        }else{
            throw new SupplyException("There is no a RawPlate in the Supply. Order other RawPlate ");
        }
    }

    public int size(){
        return provides.size();
    }

    public boolean isEmpty(){
        return provides.size()==0;
    }

    @Override
    public String toString() {
        return "Supply{" +
                "provides=" + provides +
                '}';
    }
}
