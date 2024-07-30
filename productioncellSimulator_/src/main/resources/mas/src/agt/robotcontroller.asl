!start.
!blockMovement.

+!start
    <- .print("Obtaining the token for communication and checking its properties for [ROBOT].");
       !getToken;
       !getTDProperties;
       +notMoveUpUntilPressFinish(false) [source(self)];
       !checkEmptyArm1;
       !checkEmptyArm2;
       !checkAngle;
       !robotControl.

+!blockMovement
    <-  .wait(13000);
        updateMovement(false);
        .send(press, achieve, malfunctionRobot);
        .send(depositbelt, achieve, malfunctionRobot);
        .print("Robot can't move");
        !fixMovement.

+!fixMovement
    <-  .wait(3000);
        updateMovement(true);
        .send(press, achieve, fixMalfunctionRobot);
        .send(depositbelt, achieve, fixMalfunctionRobot);
        .print("Robot can move again");
        !blockMovement.

//Picking from ert
+!robotControl
    : anglerobot(Y) & Y = 90  & emptyarm1(Z) & Z = true & pickERT(X) & X = true
    <-  .wait(1000);
        setPickERT(false);
        .print("Pick Raw Plate From ERT");
        !activateMagnet1;
        .wait(1000);
        !checkEmptyArm1;
        !robotControl.

//Picking from press
+!robotControl
    : anglerobot(Y) & Y = 90  & emptyarm2(Z) & Z = true  & pickPress(X) & X = true
    <-  .send(press,achieve,activatePress);
        -notMoveUpUntilPressFinish(true) [source(self)];
        +notMoveUpUntilPressFinish(false) [source(self)];
        !transferToPress.

+!transferToPress
    : stillMove [source(press)]
    <-   setPickPress(false);
        .print("Pick MetalPlate From Press");
        .send(press,achieve,open);
        .wait(500);
        !activateMagnet2;
        .wait(500);
        .send(press,achieve,close);
        !checkEmptyArm2;
        !robotControl.

+!transferToPress
    <-  .wait(1000);
        !startTransfer.

//Material release in DepositBelt
+!robotControl
    : anglerobot(Y) & Y = 0 & emptyarm2(X) & X = false
    <-  .send(depositbelt,achieve,activateDepositBelt);
        !startTransfer.


+!startTransfer
    :   stillRunning [source(depositbelt)]
    <-      .print("MetalPlate release in the DepositBelt");
            !activateMagnet2;
            .send(depositbelt, achieve, startDeposit);
            -stillRunning [source(depositbelt)];
            !checkEmptyArm2;
            !robotControl.

+!startTransfer
    <-  .wait(1000);
        !startTransfer.

//Material raw release in press
+!robotControl
    :  anglerobot(Y) & Y = 0 & emptyarm1(X) & X = false
    <-   .send(press,achieve,activatePress);
         -notMoveUpUntilPressFinish(false) [source(self)];
         +notMoveUpUntilPressFinish(true) [source(self)];
         !takeMaterialsFromPress.

+!takeMaterialsFromPress
    : stillMove [source(press)]
    <-  .send(press,achieve,open);
        .wait(500);
        !activateMagnet1;
        .wait(500);
        .send(press,achieve,close);
        .send(press,achieve,activatePress);
        !checkEmptyArm1;
        !robotControl.

+!takeMaterialsFromPress
    <-  .wait(1000);
        !takeMaterialsFromPress.

+!robotControl
    : anglerobot(Y) & Y > 0 & (emptyarm1(X) & X = false |  emptyarm2(Z) & Z = false)  & movement(W) & W = true & notMoveUpUntilPressFinish(V) & V = false
    <-  .wait(1000);
        .print("Rotate Up");
        !rotateUp;
        !checkAngle;
        !robotControl.

+!robotControl
    : ert [source(self)] & anglerobot(Y) & Y = 90 & emptyarm2(X) & X = true  & emptyarm1(Z) & Z = true
    <-  .send(press, achieve, malfunctionRobot);
        .send(depositbelt, achieve, malfunctionRobot);
        .print("I have no materials, the ert or FeedBelt is blocked and I wait that you reactivate");
        .wait(3000);
        !robotControl.

+!robotControl
    : anglerobot(Y) & Y < 90 & emptyarm2(X) & X = true  & emptyarm1(Z) & Z = true & movement(W) & W = true
    <-  .wait(1000);
        .print("Rotate Down");
        !rotateDown;
        !checkAngle;
        !robotControl.

+!robotControl
    <-  .wait(1000);
        .print("Robot check his propriety");
        !checkEmptyArm1;
        !checkEmptyArm2;
        !checkAngle;
        !robotControl.


+!activateRobot
    <-  .print("Input Received from ERT");.

+!activateRobotPress
    <-  .print("Input Received from Press").

+!pickFromPress
    <-  setPickPress(true).

+!pickFromERT
    <-  setPickERT(true).

+!malfunctionERT
    <-  +ert [source(self)];
        .print("Ert tells me that it have some problem").

+!fixMalfunctionERT
    <-  -ert [source(self)];
        .print("Ert tells me that it has fixed all the problems").

+!getToken
    <-  getToken.

+!getTDProperties
    <-  getTDProperties.

+!checkEmptyArm1
    <-  checkEmptyArm1.

+!checkEmptyArm2
    <-  checkEmptyArm2.

+!checkAngle
    <-  checkAngle.

+!activateMagnet1
    <-  activateMagnet1.

+!activateMagnet2
    <-  activateMagnet2.

+!rotateUp
    <-  rotateUp.

+!rotateDown
    <-  rotateDown.

-!getToken
    <-  .print("Error retrieving token for communication in the [ROBOT]");
        .wait(1000);
        !getToken.

-!getTDProperties
    <-  .print("Error retrieving propriety for communication in the [ROBOT]");
        .wait(1000);
        !getTDProperties.

-!checkEmptyArm1
    <-  .print("Error checking empty arm1 in the [ROBOT]");
        .wait(1000);
        !checkEmptyArm1.

-!checkEmptyArm2
    <-  .print("Error checking empty arm2 in the [ROBOT]");
        .wait(1000);
        !checkEmptyArm2.

-!checkAngle
    <-  .print("Error checking angle in the [ROBOT]");
        .wait(1000);
        !checkAngle.

-!activateMagnet1
    <-  .print("Error in activating magnet1 in the [ROBOT]");
        .wait(1000);
        !activateMagnet1.

-!activateMagnet2
    <-  .print("Error in activating magnet2 in the [ROBOT]");
        .wait(1000);
        !activateMagnet2.

-!rotateUp
    <-  .print("Error for rotateUp operation in the [ROBOT]");
        .wait(1000);
        !rotateUp.

-!rotateDown
    <-    .print("Error for rotateDown operation in the [ROBOT]");
          .wait(1000);
          !rotateDown.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

