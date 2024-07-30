package it.unito.di.other;

import it.unito.di.metalplate.MetalPlate;

import java.util.LinkedList;

public class Warehouse {

    private LinkedList<MetalPlate> stores;

    public Warehouse() {
        this.stores = new LinkedList<MetalPlate>();;
    }


    public LinkedList<MetalPlate> getStores() {
        return stores;
    }

    public void setStores(LinkedList<MetalPlate> stores) {
        this.stores = stores;
    }

    public void addMetalPlate(MetalPlate metalPlate){
        metalPlate.setState(7);
        getStores().addLast(metalPlate);


    }
    public int size(){
        return getStores().size();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "stores=" + stores +
                '}';
    }
}
