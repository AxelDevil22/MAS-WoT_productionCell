package it.unito.di.press;

import it.unito.di.metalplate.MetalPlate;

public class Forge extends Thread {
    private long time;
    private MetalPlate forged;
    public Forge() {
        this.time = 7 * 1000;
    }
    public Forge(long time) {
        this.time = time * 1000;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public MetalPlate getForged() {
        return forged;
    }

    public void setForged(MetalPlate forged) {
        this.forged = forged;
    }

    public void run() {

        try {

            sleep(getTime());

            forged.setForged(true);
            System.out.println("[PRESS][forgePress] Forging Done!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}