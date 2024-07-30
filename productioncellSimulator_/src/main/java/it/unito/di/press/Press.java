package it.unito.di.press;

import it.unito.di.metalplate.MetalPlate;

public class Press {

    private boolean empty;
    private boolean forging;

    private Plate composed;

    private MetalPlate forge;

    private boolean working;

    public Press() {
        this.empty = true;
        this.forging = false;
       // this.plateTop = new Plate(1);
        this.composed = new Plate(0);
        this.working = false;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }


    public boolean isForging() {
        return forging;
    }

    public void setForging(boolean forging) {
        this.forging = forging;
    }

    public Plate getPlate() {
        return composed;
    }

    public void setPlate(Plate plate) {
        this.composed = plate;
    }

    public boolean isWorking() {
        return working;
    }

    public Plate getComposed() {
        return composed;
    }

    public void setComposed(Plate composed) {
        this.composed = composed;
    }

    public MetalPlate getForge() {
        return forge;
    }

    public void setForge(MetalPlate forge) {
        this.forge = forge;
    }

    public void setWorking(boolean working) throws PressException {
        if(!working){
            if(isForging()){
                throw new PressException("The press is forging, wait before turning off the machine");
            }
        }
        this.working = working;

    }

    public boolean isOpen(){
        return getPlate().getPositionX() < 0;
    }
    public boolean isClose(){
        return getPlate().getPositionX() == 0;
    }

    public void placeMetalPlate(MetalPlate forge) throws PressException {
        if(isOpen()){
            forge.setState(4);
            setEmpty(false);
            this.forge=forge;
            }
        else{
            throw  new PressException("Machine is closed! Open the machine to place MetalPlate");
        }
    }
    public void removePlate() throws PressException {
        if(isOpen()){
            setForge(null);
            setEmpty(true);
        }else{
            throw new PressException("Machine is closed! Open the machine to take MetalPlate");
        }

    }
    public void openPress() throws PressException {

        if(isForging()){
            throw new PressException("The press is forging, wait before open the machine");

        }else{
            getPlate().setPositionX(-3);
        }

    }

    public void closePress(){
        getPlate().setPositionX(0);

    }

    public void forgeMetalPlate(MetalPlate metalPlate) throws PressException {
        if (isClose()) {
            if (metalPlate != null) {
                if (!metalPlate.isForged()) {
                    setForging(true);

                    Forge forgeProcess = new Forge();
                    forgeProcess.setForged(metalPlate);
                    forgeProcess.start();


                    try {
                        forgeProcess.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.forge = forgeProcess.getForged();
                    setForging(false);
                } else {
                    throw new PressException("There MetalPlate in the Press is also Forged");
                }

            } else {
                throw new PressException("There is no a RawPlate in the Press. Insert a RawPlate ");
            }


        } else {
            throw new PressException("Press is open! Close the Press to Forge ");
        }
    }

    @Override
    public String toString() {
        return "PRESS {" +
                "empty=" + empty +
                ", forging=" + forging +
                ", composed=" + composed +
                ", forge=" + forge +
                ", working=" + working +
                '}';
    }


}
