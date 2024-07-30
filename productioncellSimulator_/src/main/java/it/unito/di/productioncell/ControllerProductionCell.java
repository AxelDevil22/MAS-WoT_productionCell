package it.unito.di.productioncell;

import it.unito.di.belt.DepositBeltException;
import it.unito.di.belt.FeedBeltException;
import it.unito.di.elevatingrotarytable.ElevatingRotaryTableException;
import it.unito.di.server.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import it.unito.di.metalplate.MetalPlate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unito.di.other.SupplyException;
import it.unito.di.press.PlateException;
import it.unito.di.press.PressException;
import it.unito.di.robot.RobotException;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public class ControllerProductionCell {
    @FXML
    private Label RT_Angle;
    @FXML
    private Label Robot_Angle;
    @FXML
    private Label RT_Height;
    @FXML
    private Label Press_PosX;
    @FXML
    private Label feedBeltNumber;
    @FXML
    private Label rotaryTableNumber;
    @FXML
    private Label depositBeltNumber;
    @FXML
    private Label supplyNumber;
    @FXML
    private Label warehouseNumber;
    @FXML
    private Button start_demoButton;
    @FXML
    private Button manual_modeButton;
    @FXML
    private Button rotate_RTButton;
    @FXML
    private Button up_RTButton;
    @FXML
    private Button down_RTButton;
    @FXML
    private Button rotateDownRobotButton;
    @FXML
    private Button rotateUpRobotButton;
    @FXML
    private Button openPressButton;
    @FXML
    private Button forgePressButton;
    @FXML
    private Button closePressButton;
    @FXML
    private Button downPlatePressButton;
    @FXML
    private Button upPlatePressButton;
    @FXML
    private Button startFeedBeltButton;
    @FXML
    private Button stopFeedBeltButton;
    @FXML
    private Button startDepositBeltButton;
    @FXML
    private Button stopDepositBeltButton;
    @FXML
    private Button addRawPlateButton;
    @FXML
    private Button addSupplyButton;
    @FXML
    private Button storeMetalPlateButton;
    @FXML
    private Button mas_modeButton;
    /*
      @FXML
      private Button debugButton;
     */
    @FXML
    private Button transferButton;
    @FXML
    private Circle feedBeltLight;
    @FXML
    private Circle depositBeltLight;
    @FXML
    private Circle RTLight;
    @FXML
    private Circle PressLight;
    @FXML
    private Circle RobotLight;
    @FXML
    private CheckBox magnetRobot1;
    @FXML
    private CheckBox magnetRobot2;
    @FXML
    public ImageView imageCell;
    @FXML
    private ImageView ERTImage;
    @FXML
    private ImageView RobotImage;
    @FXML
    private ImageView rawPlate1;
    @FXML
    private ImageView rawPlate2;
    @FXML
    private ImageView rawPlate3;
    @FXML
    private ImageView rawPlate4;
    @FXML
    private ImageView rawPlate5;
    @FXML
    private ImageView rawPlateArm;
    @FXML
    private ImageView rawPlateERT;
    @FXML
    private ImageView rawPlatePress;
    @FXML
    private ImageView MetalPlatePress;
    @FXML
    private ImageView MetalPlateArm;
    @FXML
    private ImageView MetalPlate1;
    @FXML
    private ImageView MetalPlate2;
    @FXML
    private ImageView MetalPlate3;
    @FXML
    private ImageView MetalPlate4;
    @FXML
    private ImageView MetalPlate5;
    @FXML
    private ImageView MetalPlate6;
    @FXML
    private ImageView MetalPlate7;
    @FXML
    private ImageView rawPlateSupply;
    @FXML
    private ImageView metalPlateWareHouse;
    @FXML
    private ProgressBar progressBarPress;
    private ProductionCell productionCell;
    private double progress;
    private int imageViewFeedBeltAddIndex;
    private int imageViewDepositBeltAddIndex;
    private int imageViewFeedBeltRemoveIndex;
    private int imageViewDepositBeltRemoveIndex;
    private List<ImageView> imageViewsFeedBelt;
    private List<ImageView> imageViewsDepositBelt;
    private boolean robotMove;
    private boolean robotStart;
    private final static Logger logger  = LogManager.getLogger(ControllerProductionCell.class);

    @FXML
    void initialize()  {
        String customLogConfig = "src/main/resources/log4j/log4j2.xml";
        Configurator.initialize(null, customLogConfig);

        progress = 0.0;
        imageViewsFeedBelt = Arrays.asList(rawPlate1, rawPlate2, rawPlate3, rawPlate4, rawPlate5);
        imageViewsDepositBelt = Arrays.asList(MetalPlate7, MetalPlate6, MetalPlate5, MetalPlate4, MetalPlate3, MetalPlate2, MetalPlate1);


    }

    @FXML
    private void masMode() throws IOException {
        start_demoButton.setDisable(true);
        manual_modeButton.setDisable(true);
        start_demoButton.setVisible(false);
        manual_modeButton.setVisible(false);
        mas_modeButton.setDisable(true);



        productionCell = new ProductionCell();
        StartServer startServer = new StartServer(this);
        FeedBeltServer feedBeltServer = new FeedBeltServer(this);
        ERTServer ertServer = new ERTServer(this);
        RobotServer robotServer = new RobotServer(this);
        PressServer pressServer = new PressServer(this);
        DepositBeltServer depositBeltServer = new DepositBeltServer(this);
        startServer.startServer();
        feedBeltServer.startFeedBeltServer();
        ertServer.startERTServer();
        robotServer.startRobotServer();
        pressServer.startPressServer();
        depositBeltServer.startDepositBeltServer();


        logger.info("[MULTI AGENT SYSTEM] Selected ");
        System.out.println("[MULTI AGENT SYSTEM] Selected ");

        logger.info("[MULTI AGENT SYSTEM] Waiting for the Multi-Agent System to start");
        System.out.println("[MULTI AGENT SYSTEM] Waiting for the Multi-Agent System to start");

        String operatingSystem = System.getProperty("os.name").toLowerCase();
        System.out.println("[CHECK OPERATING SYSTEM] " + operatingSystem);
        logger.debug("[CHECK OPERATING SYSTEM] " + operatingSystem);

        if(operatingSystem.contains("win")){
            ProcessBuilder MultiAgentSystem = new ProcessBuilder("cmd.exe", "/c", "gradlew run");
            MultiAgentSystem.directory(new File("src/main/resources/mas"));
            MultiAgentSystem.start();
        }else {
            ProcessBuilder MultiAgentSystem = new ProcessBuilder("./gradlew", "run");
            MultiAgentSystem.directory(new File("src/main/resources/mas"));
            MultiAgentSystem.start();

        }
    }

    //action  for Start server
    public void activateCellProduction() throws PressException {
        Platform.runLater(() -> {
            try {
                agentSetOnProductionCell();
            } catch (PressException exception) {
                logger.error(exception);
                System.out.println(exception);
            }
        });
    }

    public void activateAddSupply(){
        addSupplyButton.setVisible(true);
        addSupplyButton.setDisable(false);
    }

    // action Feed Belt server
    public boolean checkSupply(){
        return  productionCell.getSupply().isEmpty();
    }

    public boolean checkFeedBelt(){
        return productionCell.getFeedBelt().isEmpty();
    }

    public void addMaterials(){
        Platform.runLater(this::addRawPlate);
    }

    public void changeRunningFeedBelt(Boolean condition){
        productionCell.getFeedBelt().setRunning(condition);
        if(!condition)
            feedBeltLight.setFill(Color.YELLOW);
        else
            feedBeltLight.setFill(Color.GREEN);
    }

    //ERT
    public void rotateAnimationERT()
    {
        Platform.runLater(this::rotateERT);
    }

    public void upAnimationERT()
    {
        Platform.runLater(this::upERT);
    }

    public void downAnimationERT()
    {
        Platform.runLater(this::downERT);
    }

    public boolean checkEmptyERT(){
        boolean isERTEmpty=  productionCell.getElevatingRotaryTable().isEmpty();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isERTEmpty);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isERTEmpty);
        return isERTEmpty;
    }

    public void transferTOERT(){
        Platform.runLater(this::transferTo);
    }

    public float checkAngleERT(){
        float checkAngleERT=  productionCell.getElevatingRotaryTable().getAngle();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkAngleERT);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkAngleERT);
        return checkAngleERT;
    }

    public int checkPositionERT(){
        int checkPositionYERT =  productionCell.getElevatingRotaryTable().getPositionY();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkPositionYERT);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkPositionYERT);
        return checkPositionYERT;
    }

    public void changeColorErt(Boolean condition)
    {
        if(!condition)
            RTLight.setFill(Color.YELLOW);
        else
            RTLight.setFill(Color.GREEN);
    }

    //ROBOT
    public Boolean checkArm1EMpty(){
        boolean isArm1Empty=  productionCell.getRobot().getArm1().getEquipped().isEmpty();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isArm1Empty);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isArm1Empty);
        return isArm1Empty;
    }

    public Boolean checkArm2EMpty(){
        boolean isArm2Empty=  productionCell.getRobot().getArm2().getEquipped().isEmpty();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isArm2Empty);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isArm2Empty);
        return isArm2Empty;
    }

    public float checkAngleRobot(){
        float checkAngleRobot=  productionCell.getRobot().getAngle();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkAngleRobot);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkAngleRobot);
        return checkAngleRobot;
    }

    public void activeMagnetOne(){
        Platform.runLater(this::activeMagnet1);
    }

    public void activeMagnetTwo(){
        Platform.runLater(this::activeMagnet2);
    }

    public void rotateUpRobot()
    {
        Platform.runLater(this::rotateRobotUp);
    }

    public void rotateDownRobot()
    {
        Platform.runLater(this::rotateRobotDown);
    }

    public void changeColorRobot(Boolean condition)
    {
        if(!condition)
            RobotLight.setFill(Color.YELLOW);
        else
            RobotLight.setFill(Color.GREEN);
    }

    public Boolean pressIsForging(){
        boolean isPressForging=  productionCell.getPress().isForging();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isPressForging);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isPressForging);
        return isPressForging;
    }

    public Boolean pressIsEmpty(){
        boolean isPressEmpty=  productionCell.getPress().isEmpty();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isPressEmpty);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isPressEmpty);
        return isPressEmpty;
    }

    public int checkPositionXPress(){
        int checkPositionXPress=  productionCell.getPress().getPlate().getPositionX();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkPositionXPress);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + checkPositionXPress);
        return checkPositionXPress;
    }

    public Boolean checkPressIsForged(){
        boolean isForged;
        try{
            isForged =  productionCell.getPress().getForge().isForged();
        }catch(NullPointerException exception){
            isForged=false;
        }
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isForged);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isForged);
        return isForged;
    }

    public void forgePressAnimation(){
        Platform.runLater(this::forgePress);
    }

    public void openPressAnimation(){
        Platform.runLater(() -> {
            try {
                openPress();
            } catch (PressException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closePressAnimation(){
        Platform.runLater(this::closePress);
    }


    public void changeColorPress(Boolean condition)
    {
        if(!condition)
            PressLight.setFill(Color.YELLOW);
        else
            PressLight.setFill(Color.GREEN);
    }

    //Action Deposit Belt
    public void storeMetalPlateAnimation(){
        Platform.runLater(this::storeMetalPlate);
    }

    public Boolean checkDepositBelt(){
        boolean isDepositBeltEmpty=  productionCell.getDepositBelt().isEmpty();
        System.out.println("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isDepositBeltEmpty);
        logger.debug("[MULTI AGENT SYSTEM] Info Sent to MAS: " + isDepositBeltEmpty);
        return isDepositBeltEmpty;
    }

    public void changeRunningDepositBelt(Boolean condition){
        productionCell.getDepositBelt().setRunning(condition);
        if(!condition)
            depositBeltLight.setFill(Color.YELLOW);
        else
            depositBeltLight.setFill(Color.GREEN);
    }

    private void agentSetOnProductionCell() throws PressException {
        PressLight.setFill(Color.GREEN);
        RTLight.setFill(Color.GREEN);
        RobotLight.setFill(Color.GREEN);
        feedBeltLight.setFill(Color.GREEN);
        depositBeltLight.setFill(Color.GREEN);
        productionCell.onWorking();
        productionCell.activateBelts();
    }

    @FXML
    private void startDemoSimulation() throws PressException {
        logger.info("[DEMO SIMULATION] Selected ");
        System.out.println("[DEMO SIMULATION] Selected ");

        start_demoButton.setDisable(true);
        manual_modeButton.setVisible(false);
        mas_modeButton.setVisible(false);

        addSupplyButton.setVisible(true);
        addSupplyButton.setDisable(false);


        //Light Config
        PressLight.setFill(Color.GREEN);
        RTLight.setFill(Color.GREEN);
        RobotLight.setFill(Color.GREEN);
        feedBeltLight.setFill(Color.GREEN);
        depositBeltLight.setFill(Color.GREEN);


        //Production Cell Object
        productionCell = new ProductionCell();

        productionCell.onWorking();
        productionCell.activateBelts();
        robotMove = true;
        robotStart = false;


        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.6), event -> {

            if (productionCell.getDepositBelt().getEmployedSlot() > 0) {
                storeMetalPlate();
            }
            //Spostamento rawPlate in to ERT
            if (productionCell.getElevatingRotaryTable().isEmpty() && !productionCell.getFeedBelt().isEmpty() && productionCell.getElevatingRotaryTable().getPositionY() == 0 && productionCell.getElevatingRotaryTable().getAngle() == 0) {
                transferTo();
            }
            //Spostamento rawPlate nella FeedBelt
            if (productionCell.getFeedBelt().isEmpty() && !productionCell.getSupply().isEmpty() && productionCell.getElevatingRotaryTable().isEmpty()) {
                addRawPlate();

            }

            //Magneti con Robot a 90
            if (productionCell.getRobot().getAngle() == 90) {
                if (!productionCell.getPress().isEmpty()) {
                    try {
                        openPress();
                        activeMagnet2();
                        closePress();
                    } catch (PressException exception) {
                        logger.error(exception);
                        System.out.println(exception);
                    }
                }
                if (!productionCell.getElevatingRotaryTable().isEmpty() && productionCell.getElevatingRotaryTable().getPositionY() == 10 && productionCell.getElevatingRotaryTable().getAngle() == 100) {
                    activeMagnet1();
                }
            }

            //Magneti con Robot a 0
            if (productionCell.getRobot().getAngle() == 0) {
                //Deposito RawPlate
                if (productionCell.getPress().isEmpty() && !productionCell.getRobot().getArm1().getEquipped().isEmpty()) {
                    try {
                        openPress();
                        activeMagnet1();
                        closePress();

                        forgePress();
                    } catch (PressException exception) {
                        logger.error(exception);
                        System.out.println(exception);
                    }

                }
                if (!productionCell.getRobot().getArm2().getEquipped().isEmpty()) {
                    activeMagnet2();
                }
            }

            if (productionCell.checkERT()) {
                robotStart = true;
            }
            if (!productionCell.checkPlate()) {
                robotStart = false;
            }

            //Movimento Robot Up and Down
            if (productionCell.getRobot().getAngle() > 0 && productionCell.checkPlate() && robotMove && robotStart) {
                rotateRobotUp();
                if (productionCell.getRobot().getAngle() == 0) {
                    robotMove = false;
                }
            } else if (productionCell.getRobot().getAngle() < 90 && !robotMove) {
                rotateRobotDown();
                if (productionCell.getRobot().getAngle() == 90) {
                    robotMove = true;
                }
            }

            if (productionCell.getElevatingRotaryTable().isEmpty()) {
                if (productionCell.getElevatingRotaryTable().getPositionY() > 0) {
                    downERT();
                }
                if (!(productionCell.getElevatingRotaryTable().getAngle() == 0)) {
                    rotateERT();
                }

            } else {
                if (productionCell.getElevatingRotaryTable().getPositionY() < 10) {
                    upERT();
                }
                if (!(productionCell.getElevatingRotaryTable().getAngle() == 100)) {
                    rotateERT();
                }

            }


        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


    }

    @FXML
    private void manualMode() {
        logger.info("[MANUAL MODE] Selected ");
        System.out.println("[MANUAL MODE] Selected ");
        start_demoButton.setVisible(false);
        manual_modeButton.setDisable(true);
        mas_modeButton.setVisible(false);

        //Button On
        addRawPlateButton.setDisable(false);
        addRawPlateButton.setVisible(true);
        transferButton.setVisible(true);
        addSupplyButton.setVisible(true);
        storeMetalPlateButton.setVisible(true);
        //debugButton.setDisable(false);

        //Light Config
        PressLight.setDisable(false);
        RTLight.setDisable(false);
        RobotLight.setDisable(false);
        feedBeltLight.setDisable(false);
        depositBeltLight.setDisable(false);


        //Production Cell Object
        productionCell = new ProductionCell();


    }

    @FXML
    private void testDebug() {
      


        PressLight.setDisable(false);
        RTLight.setDisable(false);
        RobotLight.setDisable(false);
        feedBeltLight.setDisable(false);
        depositBeltLight.setDisable(false);

        //Button On
        addRawPlateButton.setDisable(false);
        addRawPlateButton.setVisible(true);
        transferButton.setVisible(true);
        addSupplyButton.setVisible(true);
        storeMetalPlateButton.setVisible(true);

    }


    @FXML
    private void setFeedBeltRunning() {
        if (productionCell.getFeedBelt().isRunning()) {
            productionCell.getFeedBelt().setRunning(false);
            startFeedBeltButton.setDisable(false);
            stopFeedBeltButton.setDisable(true);

            logger.info("[FEED BELT][setFeedBeltRunning] FeedBelt is Running: " + productionCell.getFeedBelt().isRunning());
            System.out.println("[FEED BELT][setFeedBeltRunning] FeedBelt is Running: " + productionCell.getFeedBelt().isRunning());
        } else {
            productionCell.getFeedBelt().setRunning(true);
            if (productionCell.getFeedBelt().getEmployedSlot() > 0 && rawPlate1.isVisible()) {
                showAnimationFeedBelt();
            }

            startFeedBeltButton.setDisable(true);
            stopFeedBeltButton.setDisable(false);

            logger.info("[FEED BELT][setFeedBeltRunning] FeedBelt is Running: " + productionCell.getFeedBelt().isRunning());
            System.out.println("[FEED BELT][setFeedBeltRunning] FeedBelt is Running: " + productionCell.getFeedBelt().isRunning());
        }
    }

    @FXML
    private void setDepositBeltRunning() {
        if (productionCell.getDepositBelt().isRunning()) {
            productionCell.getDepositBelt().setRunning(false);

            startDepositBeltButton.setDisable(false);
            stopDepositBeltButton.setDisable(true);

            logger.info("[DEPOSIT BELT][setDepositBeltRunning] DepositBelt is Running: " + productionCell.getFeedBelt().isRunning());
            System.out.println("[DEPOSIT BELT][setDepositBeltRunning] DepositBelt is Running: " + productionCell.getFeedBelt().isRunning());
        } else {
            productionCell.getDepositBelt().setRunning(true);
            if (productionCell.getDepositBelt().getEmployedSlot() > 0 && MetalPlate1.isVisible()) {
                showAnimationDepositBelt();
            }

            startDepositBeltButton.setDisable(true);
            stopDepositBeltButton.setDisable(false);

            logger.info("[DEPOSIT BELT][setDepositBeltRunning] DepositBelt is Running: " + productionCell.getFeedBelt().isRunning());
            System.out.println("[DEPOSIT BELT][setDepositBeltRunning] DepositBelt is Running: " + productionCell.getFeedBelt().isRunning());
        }
    }

    @FXML
    private void addSupplyRawPlate() {

        MetalPlate rawPlate = new MetalPlate();
        productionCell.getSupply().addRawPlate(rawPlate);
        rawPlateSupply.setVisible(true);
        supplyNumber.setText("" + productionCell.getSupply().size());

        logger.debug("[SUPPLY][addSupplyRawPlate] The supplying has arrived in the Supply " + productionCell.getSupply().getProvides());
        System.out.println("[SUPPLY][addSupplyRawPlate] The supplying has arrived in the Supply  " + productionCell.getSupply().getProvides());

    }

    @FXML
    private void storeMetalPlate() {

        try {

            productionCell.storeMetalPlate();
            System.out.println("Deposit belt has " +productionCell.getDepositBelt().getCarries());
            System.out.println("Depsoit belt running "+ productionCell.getDepositBelt().isRunning());
            metalPlateWareHouse.setVisible(true);
            MetalPlate7.setVisible(false);
            showAnimationDepositBeltStorage();
            System.out.println("DEPOSIT BELT STORIN");


            warehouseNumber.setText("" + productionCell.getWarehouse().size());

            depositBeltNumber.setText("Number of MetalPlate: " + productionCell.getDepositBelt().getCarries().size());

            logger.debug("[WAREHOUSE][storeMetalPlate] MetalPlate Stored! ");
            System.out.println("[WAREHOUSE][storeMetalPlate] MetalPlate Stored! ");
        } catch (DepositBeltException | ProductionCellException exception) {
            logger.error(exception);
            System.out.println(exception);
        }


    }

    @FXML
    private void addRawPlate() {

        try {
            if (productionCell.getFeedBelt().getEmptySlot() > 0) {
                if (!rawPlate1.isVisible()) {

                    MetalPlate rawPlate = productionCell.getSupply().removeRawPlate();
                    productionCell.getFeedBelt().addRawPlate(rawPlate);
                    if (productionCell.getSupply().isEmpty()) {
                        rawPlateSupply.setVisible(false);
                    }

                    supplyNumber.setText("" + productionCell.getSupply().size());
                    rawPlate1.setVisible(true);

                    logger.debug("[FEED BELT][addRawPlate] RawPlate placed on the FeedBelt " + productionCell.getFeedBelt());
                    System.out.println("[FEED BELT][addRawPlate] RawPlate placed on the FeedBelt " + productionCell.getFeedBelt());
                    if (productionCell.getFeedBelt().isRunning())
                        showAnimationFeedBelt();
                    feedBeltNumber.setText("Number of MetalPlate: " + productionCell.getFeedBelt().getCarries().size());
                } else {
                    logger.error("[FEED BELT][addRawPlate] Activate the Belt for free up space to add a RawPlate");
                    System.out.println("[FEED BELT][addRawPlate] Activate the Belt for free up space to add a RawPlate");
                }
            } else {
                logger.error("[FEED BELT][addRawPlate] Max Capacity of FeedBelt");
                System.out.println("[FEED BELT][addRawPlate] Max Capacity of FeedBelt");
            }
        } catch (FeedBeltException | SupplyException exception) {
            logger.error(exception);
            System.out.println(exception);
        }
    }


    @FXML
    private void transferTo() {
        try {

            productionCell.transferToERT();
            logger.debug("[FEED BELT][transferTo] Transferred RawPlate " + productionCell.getFeedBelt());
            logger.debug("[ROTARY TABLE][transferTo] Transferred RawPlate " + productionCell.getElevatingRotaryTable());
            System.out.println("[FEED BELT][transferTo] Transferred RawPlate " + productionCell.getFeedBelt());
            System.out.println("[ROTARY TABLE][transferTo] Transferred RawPlate " + productionCell.getElevatingRotaryTable());

            rawPlate5.setVisible(false);
            rawPlateERT.setVisible(true);

            showAnimationFeedBeltTransfer();
            feedBeltNumber.setText("Number of MetalPlate: " + productionCell.getFeedBelt().getCarries().size());
            rotaryTableNumber.setText("MetalPlate: " + productionCell.getElevatingRotaryTable().getContains().getId());


            //Tooltip of ID Plate
            Tooltip tooltipIDPlate = new Tooltip();
            tooltipIDPlate.setText(productionCell.getElevatingRotaryTable().getContains().getId());
            rotaryTableNumber.setTooltip(tooltipIDPlate);

        } catch (ProductionCellException | FeedBeltException exception) {
            System.out.println(exception);
        }


    }

    private void showAnimationFeedBeltTransfer() {
        transferButton.setDisable(true);
        imageViewFeedBeltRemoveIndex = productionCell.getFeedBelt().getMaxCapacity() - 1;
        if (imageViewFeedBeltRemoveIndex >= productionCell.getFeedBelt().getEmptySlot()) {
            transferButton.setDisable(true);
            ImageView[] currentImageView = {imageViewsFeedBelt.get(imageViewFeedBeltRemoveIndex)};
            currentImageView[0].setVisible(false);
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(productionCell.getFeedBelt().getSpeed()), e -> {
                currentImageView[0].setVisible(true);
                if (imageViewFeedBeltRemoveIndex == productionCell.getFeedBelt().getEmptySlot() - 1) {
                    currentImageView[0].setVisible(false);

                    imageViewFeedBeltRemoveIndex = 4;
                    transferButton.setDisable(false);
                    timeline.stop();
                    return;
                }

                imageViewFeedBeltRemoveIndex--;
                ImageView nextImageView = imageViewsFeedBelt.get(imageViewFeedBeltRemoveIndex);
                nextImageView.setVisible(false);
                currentImageView[0] = nextImageView;
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        } else {
            transferButton.setDisable(false);
        }
    }

    @FXML
    private void changeStateFeedBelt() {

        if (productionCell.getFeedBelt().isWorking()) {
            logger.info("[FEED BELT][changeStateFeedBelt] Turn OFF and Keys Disabled");
            System.out.println("[FEED BELT][changeStateFeedBelt] Turn OFF and Keys Disabled");
            productionCell.getFeedBelt().setWorking(false);

            startFeedBeltButton.setDisable(true);
            stopFeedBeltButton.setDisable(true);

            transferButton.setDisable(true);


            feedBeltLight.setFill(Color.RED);
        } else {
            logger.info("[FEED BELT][changeStateFeedBelt] Turn ON and Keys Activated");
            System.out.println("[FEED BELT][changeStateFeedBelt] Turn ON and Keys Activated");
            productionCell.getFeedBelt().setWorking(true);

            startFeedBeltButton.setDisable(false);


            transferButton.setDisable(false);

            feedBeltLight.setFill(Color.GREEN);
        }


    }

    @FXML
    private void changeStateDepositBelt() {
        if (productionCell.getDepositBelt().isWorking()) {
            logger.info("[DEPOSIT BELT][changeStateDepositBelt] Turn OFF and Keys Disabled");
            System.out.println("[DEPOSIT BELT][changeStateDepositBelt] Turn OFF and Keys Disabled");
            productionCell.getDepositBelt().setWorking(false);
            startDepositBeltButton.setDisable(true);
            stopDepositBeltButton.setDisable(true);
            depositBeltLight.setFill(Color.RED);
        } else {
            logger.info("[DEPOSIT BELT][changeStateDepositBelt] Turn ON and Keys Activated");
            System.out.println("[DEPOSIT BELT][changeStateDepositBelt] Turn ON and Keys Activated");
            productionCell.getDepositBelt().setWorking(true);
            startDepositBeltButton.setDisable(false);
            depositBeltLight.setFill(Color.GREEN);
        }
    }

    //ELEVATING ROTARY TABLE
    @FXML
    private void changeStateRotaryTable() {

        if (productionCell.getElevatingRotaryTable().isWorking()) {
            logger.info("[ROTARY TABLE][changeStateRotaryTable] Turn OFF and Keys Disabled");
            System.out.println("[ROTARY TABLE][changeStateRotaryTable] Turn OFF and Keys Disabled");
            //ERT Elements
            productionCell.getElevatingRotaryTable().setWorking(false);
            productionCell.getElevatingRotaryTable().resetTable();
            //GUI Elements
            rotate_RTButton.setDisable(true);
            up_RTButton.setDisable(true);
            down_RTButton.setDisable(true);
            ERTImage.setRotate(0);
            RT_Height.setText("Height: " + productionCell.getElevatingRotaryTable().getPositionY());
            RT_Angle.setText("Angle: " + productionCell.getElevatingRotaryTable().getAngle() + "°");
            RTLight.setFill(Color.RED);
        } else {
            logger.info("[ROTARY TABLE][changeStateRotaryTable] Turn ON and Keys Activated");
            System.out.println("[ROTARY TABLE][changeStateRotaryTable] Turn ON and Keys Activated");
            productionCell.getElevatingRotaryTable().setWorking(true);
            rotate_RTButton.setDisable(false);
            up_RTButton.setDisable(false);
            down_RTButton.setDisable(false);
            RTLight.setFill(Color.GREEN);
        }
    }

    @FXML
    private void upERT() {
        try {
            productionCell.getElevatingRotaryTable().upTable();
            logger.debug("[ROTARY TABLE][upERT] Height: " + productionCell.getElevatingRotaryTable().getPositionY());
            System.out.println("[ROTARY TABLE][upERT] Height: " + productionCell.getElevatingRotaryTable().getPositionY());
            RT_Height.setText("Height: " + productionCell.getElevatingRotaryTable().getPositionY());
        } catch (ElevatingRotaryTableException exception) {
            logger.error(exception);
            System.out.println(exception);
        }


    }

    @FXML
    private void downERT() {
        try {
            productionCell.getElevatingRotaryTable().downTable();
            logger.debug("[ROTARY TABLE][downERT] Height: " + productionCell.getElevatingRotaryTable().getPositionY());
            System.out.println("[ROTARY TABLE][downERT] Height: " + productionCell.getElevatingRotaryTable().getPositionY());
            RT_Height.setText("Height: " + productionCell.getElevatingRotaryTable().getPositionY());
        } catch (ElevatingRotaryTableException exception) {
            logger.error(exception);
            System.out.println(exception);
        }

    }

    @FXML
    private void rotateERT() {
        productionCell.getElevatingRotaryTable().rotateTable();
        logger.debug("[ROTARY TABLE][rotateERT] Angle: " + productionCell.getElevatingRotaryTable().getAngle());
        System.out.println("[ROTARY TABLE][rotateERT] Angle: " + productionCell.getElevatingRotaryTable().getAngle());
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setByAngle(20);
        RT_Angle.setText("Angle: " + productionCell.getElevatingRotaryTable().getAngle() + "°");
        rotateTransition.setNode(ERTImage);
        rotateTransition.play();
    }

    @FXML
    private void changeStatePress() throws PressException {

        if (productionCell.getPress().isWorking()) {
            try {

                productionCell.getPress().setWorking(false);
                openPressButton.setDisable(true);
                forgePressButton.setDisable(true);
                closePressButton.setDisable(true);
                downPlatePressButton.setDisable(true);
                upPlatePressButton.setDisable(true);
                PressLight.setFill(Color.RED);
                logger.info("[PRESS][changeStatePress] Turn OFF and Keys Disabled");
                System.out.println("[PRESS][changeStatePress] Turn OFF and Keys Disabled");

            } catch (PressException exception) {
                logger.error(exception);
                System.out.println(exception);
            }


        } else {
            logger.info("[PRESS][changeStatePress] Turn ON and Keys Activated");
            System.out.println("[PRESS][changeStatePress] Turn ON and Keys Activated");

            productionCell.getPress().setWorking(true);
            openPressButton.setDisable(false);
            closePressButton.setDisable(false);
            downPlatePressButton.setDisable(false);
            upPlatePressButton.setDisable(false);
            PressLight.setFill(Color.GREEN);

        }

    }

    @FXML
    private void openPress() throws PressException {

        productionCell.getPress().openPress();

        logger.info("[PRESS][openPress] Opened");
        System.out.println("[PRESS][openPress] Opened");

        Press_PosX.setText("Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
        forgePressButton.setDisable(true);


    }

    @FXML
    private void closePress() {
        productionCell.getPress().closePress();

        logger.info("[PRESS][closePress] Closed");
        System.out.println("[PRESS][closePress] Closed");

        Press_PosX.setText("Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
        if (productionCell.getPress().getForge() != null && !start_demoButton.isVisible()) {
            forgePressButton.setDisable(false);
        }


    }

    @FXML
    private void upPlatePress() {
        try {
            productionCell.getPress().getPlate().upPlate();

            logger.debug("[PRESS][upPlatePress] Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
            System.out.println("[PRESS][upPlatePress] Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());

            Press_PosX.setText("Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
            if (productionCell.getPress().getPlate().getPositionX() == 0 && !productionCell.getPress().isEmpty()) {
                forgePressButton.setDisable(false);
            }
        } catch (PlateException exception) {
            System.out.println(exception);
        }


    }

    @FXML
    private void downPlatePress() {
        try {
            productionCell.getPress().getPlate().downPlate();

            logger.debug("[PRESS][downPlatePress] Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
            System.out.println("[PRESS][downPlatePress] Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());

            Press_PosX.setText("Position X of Plate: " + productionCell.getPress().getPlate().getPositionX());
            forgePressButton.setDisable(true);
        } catch (PlateException exception) {
            System.out.println(exception);
        }

    }

    private void disableButtonPress() {
        forgePressButton.setDisable(true);
        closePressButton.setDisable(true);
        openPressButton.setDisable(true);
        upPlatePressButton.setDisable(true);
        downPlatePressButton.setDisable(true);
    }

    private void enableButtonPress() {

        forgePressButton.setDisable(false);
        closePressButton.setDisable(false);
        openPressButton.setDisable(false);
        upPlatePressButton.setDisable(false);
        downPlatePressButton.setDisable(false);
    }

    @FXML
    private void forgePress() {

        if (!productionCell.getPress().getForge().isForged()) {

            Task<Void> task = new Task<>() {
                @Override
                public Void call() {
                    try {
                        disableButtonPress();
                        productionCell.getPress().forgeMetalPlate(productionCell.getPress().getForge());

                        logger.info("[PRESS][forgePress] Forging...");
                        System.out.println("[PRESS][forgePress] Forging...");
                        logger.debug("[PRESS][forgePress] " + productionCell.getPress().getForge());
                        System.out.println("[PRESS][forgePress] " + productionCell.getPress().getForge());

                        enableButtonPress();
                        rawPlatePress.setVisible(false);
                        MetalPlatePress.setVisible(true);

                        logger.debug("[PRESS][forgePress] Forged:  " + productionCell.getPress().getForge());
                        System.out.println("[PRESS][forgePress] Forged:  " + productionCell.getPress().getForge());
                        if (start_demoButton.isVisible() || mas_modeButton.isVisible()) {
                            disableButtonPress();
                        }

                    } catch (PressException exception) {
                        //forgePressButton.setDisable(false);

                        logger.error(exception);
                        System.out.println(exception);
                    }
                    return null;
                }
            };
            new Thread(task).start();


            progressBarPress.setStyle("-fx-accent: orange;");
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                progress += 0.14285714;
                progressBarPress.setProgress(progress);
                if (progress >= 1.0) {
                    progress = 0.0;
                    progressBarPress.setProgress(0);
                    timeline.stop();

                }

            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else {
            logger.error("[PRESS][forgePress] There is a MetalPlate in the Press already Forged");
            System.out.println("[PRESS][forgePress] There is a MetalPlate in the Press already Forged");
        }


    }

    @FXML
    private void changeStateRobot() {

        if (productionCell.getRobot().isWorking()) {
            logger.info("[ROBOT][changeStateRobot] Turn ON and Keys Disabled");
            System.out.println("[ROBOT][changeStateRobot] Turn ON and Keys Disabled");

            productionCell.getRobot().setWorking(false);
            rotateUpRobotButton.setDisable(true);
            rotateDownRobotButton.setDisable(true);
            magnetRobot1.setDisable(true);
            magnetRobot2.setDisable(true);


            RobotLight.setFill(Color.RED);


        } else {
            logger.info("[ROBOT][changeStateRobot] Turn ON and Keys Activated");
            System.out.println("[ROBOT][changeStateRobot] Turn ON and Keys Activated");
            productionCell.getRobot().setWorking(true);

            rotateUpRobotButton.setDisable(false);
            rotateDownRobotButton.setDisable(false);
            magnetRobot1.setDisable(false);
            magnetRobot2.setDisable(false);

            RobotLight.setFill(Color.GREEN);

        }
    }

    @FXML
    private void rotateRobotDown() {
        try {
            productionCell.getRobot().rotateDownRobot();

            logger.debug("[ROBOT][rotateRobotDown] Angle: " + productionCell.getRobot().getAngle());
            System.out.println("[ROBOT][rotateRobotDown] Angle: " + productionCell.getRobot().getAngle());
            Robot_Angle.setText("Angle: " + productionCell.getRobot().getAngle() + "°");
            RotateTransition rotateTransitionRobot = new RotateTransition();

            rotateTransitionRobot.setByAngle(18);

            rotateTransitionRobot.setNode(RobotImage);
            rotateTransitionRobot.play();


        } catch (RobotException exception) {
            logger.error(exception);
            System.out.println(exception);
        }


    }

    @FXML
    private void rotateRobotUp() {
        try {
            productionCell.getRobot().rotateUpRobot();

            logger.debug("[ROBOT][rotateRobotUp] Angle: " + productionCell.getRobot().getAngle());
            System.out.println("[ROBOT][rotateRobotUp] Angle: " + productionCell.getRobot().getAngle());

            Robot_Angle.setText("Angle: " + productionCell.getRobot().getAngle() + "°");
            RotateTransition rotateTransitionRobot = new RotateTransition(Duration.millis(500));

            rotateTransitionRobot.setByAngle(-18);

            rotateTransitionRobot.setNode(RobotImage);
            // rotateTransitionRobot.setDuration(Duration.millis(100));
            rotateTransitionRobot.play();
        } catch (RobotException exception) {
            logger.error(exception);
            System.out.println(exception);
        }

    }

    @FXML
    private void activeMagnet1() {

        if (productionCell.getRobot().getArm1().getEquipped().isWorking()) { //Magnet ON->OFF

            if (productionCell.getRobot().getArm1().getEquipped().isEmpty()) {//Magnet Empty
                magnetRobot1.setSelected(false);
                productionCell.getRobot().getArm1().getEquipped().setWorking(false);

                logger.debug("[ROBOT][activeMagnet1] ARM1 Magnet is Working: " + productionCell.getRobot().getArm1().getEquipped().isWorking());
                System.out.println("[ROBOT][activeMagnet1] ARM1 Magnet is Working: " + productionCell.getRobot().getArm1().getEquipped().isWorking());
            } else {//Magnet FULL && press Empty
                if (productionCell.getRobot().getAngle() == 0 && productionCell.getPress().isEmpty() && productionCell.getRobot().getArm1().getEquipped().getAttract() != null) {
                    if (productionCell.getPress().isOpen()) {
                        try {
                            MetalPlate metalPlate = productionCell.getRobot().getArm1().getEquipped().getAttract();
                            productionCell.getRobot().detachPlateArm1();

                            logger.debug("[ROBOT][activeMagnet1] ARM1 Detached RawPlate " + productionCell.getRobot());
                            System.out.println("[ROBOT][activeMagnet1] ARM1 Detached RawPlate " + productionCell.getRobot());

                            productionCell.getPress().placeMetalPlate(metalPlate);
                            rawPlateArm.setVisible(false);
                            rawPlatePress.setVisible(true);

                            magnetRobot1.setSelected(false);
                            productionCell.getRobot().getArm1().getEquipped().setWorking(false);

                            logger.debug("[PRESS][activeMagnet1] ARM1 RawPlate placed in the Press " + productionCell.getPress());
                            System.out.println("[PRESS][activeMagnet1] ARM1 RawPlate placed in the Press " + productionCell.getPress());


                        } catch (PressException exception) {
                            logger.error(exception);
                            System.out.println(exception);
                        }

                    } else {
                        magnetRobot1.setSelected(true);
                        logger.error("[ROBOT][activeMagnet1] ARM1 cannot release the RawPlate in the Press because the Press is Closed!");
                        System.out.println("[ROBOT][activeMagnet1] ARM1 cannot release the RawPlate in the Press because the Press is Closed!");
                    }

                } else {
                    magnetRobot1.setSelected(true);
                    logger.error("[ROBOT][activeMagnet1] ARM1 cannot release the RawPlate in this position and cannot be Turned Off ");
                    System.out.println("[ROBOT][activeMagnet1] ARM1 cannot release the RawPlate in this position and cannot be Turned Off ");
                    //System.out.println("[ROBOT][activeMagnet1] ARM1 Magnet is Working: " + productionCell.getRobot().getArm1().getEquipped().isWorking());
                }
            }
        } else { //OFF->ON
            productionCell.getRobot().getArm1().getEquipped().setWorking(true);
            magnetRobot1.setSelected(true);

            logger.debug("[ROBOT][activeMagnet1] ARM1 Magnet is Working: " + productionCell.getRobot().getArm1().getEquipped().isWorking());
            System.out.println("[ROBOT][activeMagnet1] ARM1 Magnet is Working: " + productionCell.getRobot().getArm1().getEquipped().isWorking());
            try {
                if (productionCell.getRobot().getAngle() == 90 && productionCell.getElevatingRotaryTable().getPositionY() == 10 && productionCell.getElevatingRotaryTable().getAngle() == 100) {
                    if (productionCell.getElevatingRotaryTable().getContains() != null) {

                        MetalPlate metalPlate = productionCell.getElevatingRotaryTable().getContains();
                        productionCell.getElevatingRotaryTable().setContains(null);
                        productionCell.getRobot().attachPlateArm1(metalPlate);

                        rawPlateArm.setVisible(true);
                        rawPlateERT.setVisible(false);

                        magnetRobot1.setSelected(true);
                        logger.debug("[ROBOT][activeMagnet1] ARM1 Attached RawPlate " + productionCell.getRobot().getArm1());
                        System.out.println("[ROBOT][activeMagnet1] ARM1 Attached RawPlate " + productionCell.getRobot().getArm1());

                        rotaryTableNumber.setText("MetalPlate: ");
                        rotaryTableNumber.setTooltip(null);

                    }
                }
            } catch (RobotException exception) {
                logger.error(exception);
                System.out.println(exception);
            }
        }

    }

    @FXML
    private void activeMagnet2() {

        if (productionCell.getRobot().getArm2().getEquipped().isWorking()) { //Magnet ON->OFF

            if (productionCell.getRobot().getArm2().getEquipped().isEmpty()) {//Magnet Empty
                magnetRobot2.setSelected(false);
                productionCell.getRobot().getArm2().getEquipped().setWorking(false);

                logger.debug("[ROBOT][activeMagnet2] ARM2 Magnet is Working: " + productionCell.getRobot().getArm2().getEquipped().isWorking());
                System.out.println("[ROBOT][activeMagnet2] ARM2 Magnet is Working: " + productionCell.getRobot().getArm2().getEquipped().isWorking());
            } else {//Magnet FULL

                if (productionCell.getRobot().getAngle() == 0 && productionCell.getRobot().getArm2().getEquipped().getAttract() != null) {

                    try {
                        if (productionCell.getDepositBelt().getEmptySlot() > 0) {
                            if (!MetalPlate1.isVisible()) {
                                MetalPlate metalPlate = productionCell.getRobot().getArm2().getEquipped().getAttract();
                                productionCell.getDepositBelt().insertPlate(metalPlate);
                                productionCell.getRobot().detachPlateArm2();

                                logger.debug("[ROBOT][activeMagnet2] ARM2 Detached MetalPlate " + productionCell.getRobot());
                                System.out.println("[ROBOT][activeMagnet2] ARM2 Detached MetalPlate " + productionCell.getRobot());

                                MetalPlate1.setVisible(true);
                                MetalPlateArm.setVisible(false);


                                magnetRobot2.setSelected(false);
                                productionCell.getRobot().getArm2().getEquipped().setWorking(false);

                                //Animation
                                if (productionCell.getDepositBelt().isRunning()) {
                                    showAnimationDepositBelt();
                                }

                                /////////////

                                logger.debug("[DEPOSIT BELT][activeMagnet2] ARM2 placed MetalPlate in DepositBelt  " + productionCell.getDepositBelt());
                                System.out.println("[DEPOSIT BELT][activeMagnet2] ARM2 placed MetalPlate in DepositBelt  " + productionCell.getDepositBelt());
                                depositBeltNumber.setText("Number of MetalPlate: " + productionCell.getDepositBelt().getCarries().size());
                            } else {
                                magnetRobot2.setSelected(true);

                                logger.error("[DEPOSIT BELT][activeMagnet2] Activate the Belt for free up space to add a MetalPlate");
                                System.out.println("[DEPOSIT BELT][activeMagnet2] Activate the Belt for free up space to add a MetalPlate");
                            }
                        } else {
                            magnetRobot2.setSelected(true);

                            logger.error("[DEPOSIT BELT][activeMagnet2] Max Capacity of Deposit Belt");
                            System.out.println("[DEPOSIT BELT][activeMagnet2] Max Capacity of Deposit Belt");
                        }

                    } catch (DepositBeltException exception) {
                        magnetRobot2.setSelected(true);

                        logger.error(exception);
                        System.out.println(exception);
                    }

                } else {
                    magnetRobot2.setSelected(true);

                    logger.error("[ROBOT][activeMagnet2] ARM2 cannot release the MetalPlate in this position and cannot be Turned Off ");
                    System.out.println("[ROBOT][activeMagnet2] ARM2 cannot release the MetalPlate in this position and cannot be Turned Off ");
                    //System.out.println("[ROBOT][activeMagnet2] ARM2 Magnet is Working: " + productionCell.getRobot().getArm2().getEquipped().isWorking());
                }


            }
        } else { //OFF->ON
            productionCell.getRobot().getArm2().getEquipped().setWorking(true);
            magnetRobot2.setSelected(true);

            logger.debug("[ROBOT][activeMagnet2] ARM2 Magnet is Working: " + productionCell.getRobot().getArm2().getEquipped().isWorking());
            System.out.println("[ROBOT][activeMagnet2] ARM2 Magnet is Working: " + productionCell.getRobot().getArm2().getEquipped().isWorking());
            try {
                if (productionCell.getRobot().getAngle() == 90 && !productionCell.getPress().isForging() && productionCell.getPress().isOpen()) {
                    if (productionCell.getPress().getForge() != null && productionCell.getPress().getForge().isForged()) {

                        MetalPlate metalPlate = productionCell.getPress().getForge();
                        productionCell.getPress().removePlate();
                        productionCell.getRobot().attachPlateArm2(metalPlate);
                        MetalPlateArm.setVisible(true);
                        MetalPlatePress.setVisible(false);
                        magnetRobot2.setSelected(true);
                        logger.debug("[PRESS][activeMagnet2] ARM2 Picked Up MetalPlate from Press " + productionCell.getPress());
                        System.out.println("[PRESS][activeMagnet2] ARM2 Picked Up MetalPlate from Press " + productionCell.getPress());
                        logger.debug("[ROBOT][activeMagnet2] ARM2 Attached MetalPlate " + productionCell.getRobot().getArm2());
                        System.out.println("[ROBOT][activeMagnet2] ARM2 Attached MetalPlate " + productionCell.getRobot().getArm2());
                    }
                }

            } catch (RobotException | PressException exception) {
                logger.error(exception);
                System.out.println(exception);
            }


        }


    }

    private void showAnimationDepositBelt() {

        imageViewDepositBeltAddIndex = productionCell.getDepositBelt().getMaxCapacity() - 1;
        if (imageViewDepositBeltAddIndex >= productionCell.getDepositBelt().getEmployedSlot()) {

            ImageView[] currentImageView = {imageViewsDepositBelt.get(imageViewDepositBeltAddIndex)};
            currentImageView[0].setVisible(true);
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(productionCell.getDepositBelt().getSpeed()), e -> {
                currentImageView[0].setVisible(false);

                if (imageViewDepositBeltAddIndex == productionCell.getDepositBelt().getEmployedSlot() - 1) {

                    currentImageView[0].setVisible(true);

                    imageViewDepositBeltAddIndex = productionCell.getDepositBelt().getMaxCapacity() - 1;

                    timeline.stop();
                    return;
                }

                imageViewDepositBeltAddIndex--;
                ImageView nextImageView = imageViewsDepositBelt.get(imageViewDepositBeltAddIndex);
                nextImageView.setVisible(true);
                currentImageView[0] = nextImageView;
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    private void showAnimationFeedBelt() {
        addRawPlateButton.setDisable(true);
        transferButton.setDisable(true);
        imageViewFeedBeltAddIndex = 0;
        if (imageViewFeedBeltAddIndex <= productionCell.getFeedBelt().getEmptySlot()) {
            ImageView[] currentImageView = {imageViewsFeedBelt.get(imageViewFeedBeltAddIndex)};
            currentImageView[0].setVisible(true);
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(productionCell.getFeedBelt().getSpeed()), e -> {
                currentImageView[0].setVisible(false);
                if (imageViewFeedBeltAddIndex == productionCell.getFeedBelt().getEmptySlot()) {
                    currentImageView[0].setVisible(true);

                    imageViewFeedBeltAddIndex = 0;
                    addRawPlateButton.setDisable(false);
                    transferButton.setDisable(false);
                    timeline.stop();
                    return;
                }
                imageViewFeedBeltAddIndex++;
                ImageView nextImageView = imageViewsFeedBelt.get(imageViewFeedBeltAddIndex);
                nextImageView.setVisible(true);
                currentImageView[0] = nextImageView;
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        }
    }

    private void showAnimationDepositBeltStorage() {
        System.out.println("Sto muovendo");
        storeMetalPlateButton.setDisable(true);
        imageViewDepositBeltRemoveIndex = 0;
        if (imageViewDepositBeltRemoveIndex <= productionCell.getDepositBelt().getEmployedSlot() - 1) {
            storeMetalPlateButton.setDisable(true);
            ImageView[] currentImageView = {imageViewsDepositBelt.get(imageViewDepositBeltRemoveIndex)};
            currentImageView[0].setVisible(false);
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(productionCell.getDepositBelt().getSpeed()), e -> {
                currentImageView[0].setVisible(true);
                if (imageViewDepositBeltRemoveIndex == productionCell.getDepositBelt().getEmployedSlot()) {
                    currentImageView[0].setVisible(false);

                    imageViewDepositBeltRemoveIndex = 0;
                    storeMetalPlateButton.setDisable(false);
                    timeline.stop();
                    return;
                }

                imageViewDepositBeltRemoveIndex++;
                ImageView nextImageView = imageViewsDepositBelt.get(imageViewDepositBeltRemoveIndex);
                nextImageView.setVisible(false);
                currentImageView[0] = nextImageView;
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        } else {
            storeMetalPlateButton.setDisable(false);
        }
    }
}



