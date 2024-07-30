package it.unito.di.robot;

import it.unito.di.metalplate.MetalPlate;

public class Robot {

    private float angle;

    private Arm arm1;
    private Arm arm2;


    private boolean working;

    public Robot() {
        this.angle = 90;
        this.arm1 = new Arm();
        this.arm2 = new Arm();
        this.working = false;

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Arm getArm1() {
        return arm1;
    }

    public void setArm1(Arm arm1) {
        this.arm1 = arm1;
    }

    public Arm getArm2() {
        return arm2;
    }

    public void setArm2(Arm arm2) {
        this.arm2 = arm2;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void rotateDownRobot() throws RobotException {
        if (getAngle() == 90) {
            throw new RobotException("Maximum angle reached");
        } else {
            setAngle(getAngle() + 18);
        }


    }

    public void rotateUpRobot() throws RobotException {
        if (getAngle() == 0) {
            throw new RobotException("Minimum angle reached");
        } else {
            setAngle(getAngle() - 18);
        }


    }

    public void attachPlateArm1(MetalPlate metalPlate) throws RobotException {

        if (metalPlate.getState() == 2) {

            if (getArm1().getEquipped().isWorking()) {
                getArm1().getEquipped().setEmpty(false);
                getArm1().getEquipped().setAttract(metalPlate);
                getArm1().getEquipped().getAttract().setState(3);//Robot - Arm 1


            } else {
                throw new RobotException("Arm1 Electromagnet OFF");
            }


        } else {
            throw new RobotException("Plate is not in Elevating Rotary Table");
        }
    }

    public void detachPlateArm1(){

      getArm1().getEquipped().setEmpty(true);
      getArm1().getEquipped().setAttract(null);


    }

    public void attachPlateArm2(MetalPlate metalPlate) throws RobotException {
        if (metalPlate.getState() == 4) {

            if (getArm2().getEquipped().isWorking()) {
                getArm2().getEquipped().setEmpty(false);
                getArm2().getEquipped().setAttract(metalPlate);
                getArm2().getEquipped().getAttract().setState(5);//Robot - Arm 2


            } else {
                throw new RobotException("Arm2 Electromagnet OFF");
            }
        } else {
            throw new RobotException("Plate is not in the Press");
        }
    }

    public void detachPlateArm2(){

        getArm2().getEquipped().setEmpty(true);
        getArm2().getEquipped().setAttract(null);


    }


    @Override
    public String toString() {
        return "ROBOT {" +
                "angle=" + angle +
                ", arm1=" + arm1 +
                ", arm2=" + arm2 +
                ", working=" + working +
                '}';
    }


}
