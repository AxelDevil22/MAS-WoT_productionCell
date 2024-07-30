package it.unito.di.productioncell;

import it.unito.di.belt.DepositBelt;
import it.unito.di.belt.DepositBeltException;
import it.unito.di.belt.FeedBelt;
import it.unito.di.belt.FeedBeltException;
import it.unito.di.elevatingrotarytable.ElevatingRotaryTable;
import it.unito.di.metalplate.MetalPlate;
import it.unito.di.other.Supply;
import it.unito.di.other.Warehouse;
import it.unito.di.press.Press;
import it.unito.di.press.PressException;
import it.unito.di.robot.Robot;

import java.util.Objects;

public class ProductionCell {

    private FeedBelt feedBelt;
    private DepositBelt depositBelt;
    private Press press;
    private Robot robot;
    private ElevatingRotaryTable elevatingRotaryTable;
    private Warehouse warehouse;
    private Supply supply;

    public ProductionCell() {
        this.feedBelt = new FeedBelt();
        this.depositBelt = new DepositBelt();
        this.press = new Press();
        this.robot = new Robot();
        this.elevatingRotaryTable = new ElevatingRotaryTable();
        this.warehouse = new Warehouse();
        this.supply = new Supply();
    }

    public FeedBelt getFeedBelt() {
        return feedBelt;
    }

    public void setFeedBelt(FeedBelt feedBelt) {
        this.feedBelt = feedBelt;
    }

    public DepositBelt getDepositBelt() {
        return depositBelt;
    }

    public void setDepositBelt(DepositBelt depositBelt) {
        this.depositBelt = depositBelt;
    }

    public Press getPress() {
        return press;
    }

    public void setPress(Press press) {
        this.press = press;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public ElevatingRotaryTable getElevatingRotaryTable() {
        return elevatingRotaryTable;
    }

    public void setElevatingRotaryTable(ElevatingRotaryTable elevatingRotaryTable) {
        this.elevatingRotaryTable = elevatingRotaryTable;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Supply getSupply() {
        return supply;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
    }

    public void transferToERT() throws ProductionCellException, FeedBeltException {
        if(getFeedBelt().isRunning()){
            if(!getFeedBelt().getCarries().isEmpty() ){
                if(getElevatingRotaryTable().isEmpty()) {
                    if (getElevatingRotaryTable().getPositionY() == 0) {
                        if (getElevatingRotaryTable().getAngle() == 0) {
                            MetalPlate rawPlate = getFeedBelt().getCarries().getFirst();
                            getFeedBelt().removeRawPlate(rawPlate);
                            getElevatingRotaryTable().transferTo(rawPlate);
                        } else {
                            throw new ProductionCellException("Elevating Rotary Table is not at the right Angle - Angle: " + getElevatingRotaryTable().getAngle());
                        }

                    } else {
                        throw new ProductionCellException("Elevating Rotary Table is not at the right Height - Height: " + getElevatingRotaryTable().getPositionY());
                    }
                }else{
                    throw new ProductionCellException("Elevating Rotary Table is Full");
                }

            }else{
                throw new ProductionCellException("FeedBelt is Empty");
            }
        }else{
            throw new ProductionCellException("Activate the FeedBelt to Transfer RawPlate in Elevating Rotary Table");
        }




    }

    public void storeMetalPlate() throws ProductionCellException, DepositBeltException {
        if(getDepositBelt().isRunning()){
            MetalPlate metalPlate= getDepositBelt().removeMetalPlate();
            getWarehouse().addMetalPlate(metalPlate);
        }else{
            throw new ProductionCellException("Activate the DepositBelt to storage MetalPlate");
        }

    }

    public void onWorking() throws PressException {

        feedBelt.setWorking(true);
        depositBelt.setWorking(true);
        press.setWorking(true);
        robot.setWorking(true);
        elevatingRotaryTable.setWorking(true);

    }

    public void offWorking() throws PressException {

        feedBelt.setWorking(false);
        depositBelt.setWorking(false);
        press.setWorking(false);
        robot.setWorking(false);
        elevatingRotaryTable.setWorking(false);

    }

    public void activateBelts(){
        feedBelt.setRunning(true);
        depositBelt.setRunning(true);
    }

    public void disableBelts(){
        feedBelt.setRunning(false);
        depositBelt.setRunning(false);
    }

    public boolean checkPlate() {

        return !getRobot().getArm1().getEquipped().isEmpty() || !getRobot().getArm2().getEquipped().isEmpty() || !getPress().isEmpty() || !getElevatingRotaryTable().isEmpty();
    }

    public boolean checkERT(){

        return getElevatingRotaryTable().getPositionY() == 10 && getElevatingRotaryTable().getAngle() == 100;
    }

    @Override
    public String toString() {
        return "ProductionCell{" +
                "feedBelt=" + feedBelt +
                ", depositBelt=" + depositBelt +
                ", press=" + press +
                ", robot=" + robot +
                ", elevatingRotaryTable=" + elevatingRotaryTable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionCell that = (ProductionCell) o;
        return Objects.equals(feedBelt, that.feedBelt) && Objects.equals(depositBelt, that.depositBelt) && Objects.equals(press, that.press) && Objects.equals(robot, that.robot) && Objects.equals(elevatingRotaryTable, that.elevatingRotaryTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedBelt, depositBelt, press, robot, elevatingRotaryTable);
    }


}
