!start.
!blockMovement.

+!start
    <- .print("Obtaining the token for communication and checking its properties for [PRESS].");
       !getToken;
       !getTDProperties;
       !checkEmpty;
       !checkPositionX;
       !checkForging;
       !checkIsForged;
       !controlPress.

+!blockMovement
    <-  .wait(4000);
        !updateMovement(false);
        .print("Press cannot close or open");
        !fixMovement.

+!fixMovement
    <-  .wait(3000);
        updateMovement(true);
        .print("Press can close or open again");
        !blockMovement.

 +!updateMovement(X)
     : stopMalfunction [source(self)]
     <-  .wait(1000);
         !updateMovement(X).

 +!updateMovement(X)
     <-  updateMovement(X);
         !fixMovement.

+!controlPress
    : empty(Y) & Y = false & forging(Z) & Z = false & positionX(X) & X = 0 & isForged(W) & W=false
    <-  .print("Forge ");
        !forgePlate;
        setCondition;
        .wait(7000);
        .send(robot,achieve,pickFromPress);
        .send(robot,achieve,activateRobotPress);
        !controlPress.

+!controlPress
    : robot [source(self)] & empty(X) & X = true
    <- .print("I have no materials, the robot or previous agent is blocked and I wait that you reactivate");
       .wait(3000);
       !controlPress.

+!controlPress
    <-  .wait(1000);
        !checkEmpty;
        !checkPositionX;
        !checkForging;
        !checkIsForged;
        !controlPress.

+!open
    : movement(W) & W = true
    <-   .print("Open");
         !openPress.

+!open
    <-    .wait(1000);
           !open.

+!close
    : movement(W) & W = true
    <-  -stopMalfunction [source(self)];
        .print("Close");
         !closePress.

+!close
    <-    .wait(1000);
           !close.


+!activatePress
    <-  .print("Input Received from robot");
         !response.

+!response
    : movement (X) & X = true
    <-  +stopMalfunction [source(self)];
        .send(robot, tell, stillMove).

+!response
    <- .wait(1000);
        !response.

+!malfunctionRobot
    <-  +robot [source(self)];
        .print("Robot tells me that it have some problem").

+!fixMalfunctionRobot
    <-   -robot [source(self)];
        .print("Robot tells me that it has fixed all the problems").

+!getToken
    <-  getToken.

+!getTDProperties
    <-  getTDProperties.

+!openPress
    <-  openPress.

+!closePress
    <-  closePress.

+!forgePlate
    <-  forgePlate.

+!checkEmpty
    <-  checkEmpty.

+!checkPositionX
    <-  checkPositionX.

+!checkForging
    <-  checkForging.

+!checkIsForged
    <-   checkIsForged.

-!getToken
    <-  .wait(1000);
        !getToken.

-!getTDProperties
    <-  .wait(1000);
        !getTDProperties.

-!openPress
    <-  .wait(1000);
        !open.

-!closePress
    <-  .wait(1000);
        !close.

-!forgePlate
    <-  .wait(1000);
        !forgePlate.

-!checkEmpty
    <-  .wait(1000);
        !checkEmpty.

-!checkPositionX
    <-  .wait(1000);
        !checkPositionX.

-!checkForging
    <-  .wait(1000);
        !checkForging.

-!checkIsForged
    <-   .wait(1000);
         !checkIsForged.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }


