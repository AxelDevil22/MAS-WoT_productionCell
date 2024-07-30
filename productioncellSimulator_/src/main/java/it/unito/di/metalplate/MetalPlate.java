package it.unito.di.metalplate;

import java.util.Objects;
import java.util.UUID;

public class MetalPlate {

    private String id;
    private boolean forged;

    private int state;

    /* State Legend:
    *  0: Start
    *  1: Feed Belt
    *  2: Elevating Rotary Table
    *  3: Robot - Arm 1
    *  4: Press
    *  5: Robot - Arm 2
    *  6: Deposit Belt
    *  7: Finish
    *  8: Error */


    public MetalPlate() {
        this.id= UUID.randomUUID().toString();;
        this.forged = false;
        this.state = 0;
    }
    public MetalPlate(int state) {
        this.id = UUID.randomUUID().toString();;
        this.forged = false;
        this.state = state;
    }
    public MetalPlate(int state,boolean bool) {
        this.id= UUID.randomUUID().toString();;
        this.forged = bool;
        this.state = state;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isForged() {
        return forged;
    }

    public void setForged(boolean forged) {
        this.forged = forged;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MetalPlate{" +
                "id='" + id + '\'' +
                ", forged=" + forged +
                ", state=" + state +
                '}' ;
    }
}
