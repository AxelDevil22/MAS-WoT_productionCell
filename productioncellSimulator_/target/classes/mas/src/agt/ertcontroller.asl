!start.
!blockMovement.

+!start
    <- .print("Obtaining the token for communication and checking its properties for [ERT].");
       !getToken;
       !getTDProperties;
       !checkEmpty;
       !checkAngle;
       !checkPositionY;
       !control.

+!blockMovement
    <-  .wait(15000);
        updateMovement(false);
        .send(robot, achieve, malfunctionERT);
        !fixMovement.

+!fixMovement
    <-  .wait(3000);
        updateMovement(true);
        .send(robot, achieve, fixMalfunctionERT);
        !blockMovement.

+!control
    : (empty(Y) & Y == true & angle(X) & X = 0 & positionY(Z) & Z = 0)
    <-  .print("Attending the RawPlate");
        .send(feedbelt,achieve,doRequest);
        .wait({+!activeERT});
        .print("Transfert RawPlate");
        !transferTo;
        !checkEmpty;
        !checkAngle;
        !checkPositionY;
        !control.

+!control
     : angle(X) & X < 100 & empty(Y) & Y = false & movement(W) & W = true
     <-  .wait(900);
         .print("Rotate to 100 Angle");
         !rotate;
         !checkEmpty;
         !checkAngle;
         !checkPositionY;
         !control.

+!control
    : positionY(X) & X < 10 & empty(Y) & Y = false & movement(W) & W = true
    <-  .wait(400);
        .print("Up");
        !up;
        !checkPositionY;
        !checkEmpty;
        !control.

+!control
    : empty(Y) & Y = true & angle(X) & X >= 100 & movement(W) & W = true
    <-  .wait(900);
        .print("Rotate to 0 Angle");
        !rotate;
        !checkEmpty;
        !checkAngle;
        !checkPositionY;
        !control.

+!control
    : empty(Y) & Y = true & angle(X) & X = 0 & positionY(Z) & (Z <= 10 & Z > 0) & movement(W) & W = true
    <-  .wait(400);
        .print("Down");
        !down;
        !checkPositionY;
        !checkEmpty;
        !control.

+!control
    : empty(Y) & Y = false & angle(X) & X = 100 & positionY(Z) & Z = 10
    <-  .print("Input send to Robot");
        .send(robot,achieve,pickFromERT);
        .send(robot,achieve,activateRobot);
        .wait(1000);
        !checkEmpty;
        !checkAngle;
        !checkPositionY;
        !control.

+!control
    : feedbelt [source(self)] & empty(X) & X = true & positionY(Z) & Z = 0 &  angle(Y) & Y = 0
    <-  .send(robot, achieve, malfunctionERT);
        .print("I have no materials, the feedBelt is blocked and I wait that you reactivate");
        .wait(3000);
        !control.

+!control
    <-  .wait(1000);
        !checkEmpty;
        !checkAngle;
        !checkPositionY;
        !control.

+!activeERT
    <-  .print("Input Received from FeedBelt").

+!malfunctionFeedBelt
    <-  +feedbelt [source(self)];
        .print("FeedBelt tells me that it have some problem").

+!fixMalfunctionFeedBelt
    <-   -feedbelt [source(self)];
        .print("FeedBelt tells me that it has fixed all the problems").

+!getToken
    <- getToken.

+!getTDProperties
    <- getTDProperties.

+!checkEmpty
    <- checkEmpty.

+!checkAngle
    <- checkAngle.

+!checkPositionY
    <- checkPositionY.

+!transferTo
    <- transferTo.

+!up
    <- up.

+!down
    <- down.

+!rotate
    <- rotate.

-!getToken
    <- .print("Error retrieving token for communication in the [ERT]");
       .wait(1000);
       !getToken.

-!getTDProperties
    <-  .print("Error retrieving propriety for communication in the [ERT]");
        .wait(1000);
        !getTDProperties.

-!checkEmpty
    <-  .print("Error checking empty for communication in the [ERT]");
        .wait(1000);
        !checkEmpty.

-!checkAngle
    <-  .print("Error checking angle for communication in the [ERT]");
        .wait(1000);
        !checkAngle.

-!checkPositionY
    <-  .print("Error checking position y for communication in the [ERT]");
        .wait(1000);
        !checkPositionY.

-!transferTo
    <-  .print("Error for transferTO operation in the [ERT]");
        .wait(1000);
        !transferTo.

-!up
    <- .print("Error for up operation in the [ERT]");
       .wait(1000);
       !up.

-!down
    <- .print("Error for down operation in the [ERT]");
       .wait(1000);
       !down.

-!rotate
    <- .print("Error for rotate operation in the [ERT]");
       .wait(1000);
       !rotate.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }