!start.
!createMalfunctionRunning.

//Loop for malfunction Propriety = running
+!createMalfunctionRunning
    <-  .wait(12000);
        !changeStateOfRunning(false);
        .print("There is some problem for running the DepositBelt");
        !fixMalfunctionRunning.

+!fixMalfunctionRunning
    <-  .wait(6000);
        changeStateOfRunning(true);
        .print("Fix running problem for DepositBelt");
        !createMalfunctionRunning.

+!changeStateOfRunning(X)
    : stopMalfunction [source(self)]
    <-  .wait(1000);
        !changeStateOfRunning(X).

+!changeStateOfRunning(X)
    <-  changeStateOfRunning(X);
        !fixMalfunctionRunning.


+!start
<- .print("Obtaining the token for communication and checking its properties for [DEPOSITBELT]");
    !getToken;
    !getTDProperties;
    !checkDepositBelt;
    !control.

//Control for storage plate in warehouse
+!control
: empty(X) & X = false & startDeposit [source(self)]
<-  .print("MetalPlate Stored in warehouse");
    !storeMetalPlate;
    !checkDepositBelt;
    -startDeposit [source(self)];
    -stopMalfunction [source(self)];
    !control.

+!control
    : robot [source(self)] & empty(X) & X = true
    <- .print("I have no materials, the robot or previous agent is blocked and I wait that you reactivate");
       .wait(3000);
       !control.

+!control
    <- .wait(1000);
       !checkDepositBelt;
       !control.

+!activateDepositBelt
    <-  .print("Input Received from robot");
         !response.

+!response
    : running (X) & X = true
    <-  +stopMalfunction [source(self)];
        .send(robot, tell, stillRunning).

+!response
    <- .wait(1000);
        !response.

+!startDeposit
    <-  .wait(2300);
        +startDeposit.

+!malfunctionRobot
    <-  +robot [source(self)];
        .print("Robot tells me that it have some problem").

+!fixMalfunctionRobot
    <-  -robot [source(self)];
        .print("Robot tells me that it has fixed all the problems").

+!getToken
    <- getToken.

+!getTDProperties
    <-  getTDProperties.

+!checkDepositBelt
    <-  checkDepositBelt.

+!storeMetalPlate
    <-  storeMetalPlate.

-!checkDepositBelt
<-  .print("Error for checkEmpty propriety operation in the [DEPOSITBELT]");
    .wait(1000);
    !checkDepositBelt.

-!storeMetalPlate
<-  .print("Error for storeMetalPlate operation in the [DEPOSITBELT]");
    .wait(1000);
    !storeMetalPlate.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl")}