!start.
!createMalfunctionRunning.

+!start
    <-  .print("Obtaining the token for communication and checking its properties for [FEEDBELT].");
        !getToken;
        !getTDProperties;
        !checkSupply;
        !checkFeedBelt;
        !controlFeedBelt.

//Loop for malfunction Propriety = running
+!createMalfunctionRunning
    <-  .wait(12000);
        !changeStateOfRunning(false);
        .send(ert, achieve, malfunctionFeedBelt);
        .print("There is some problem for running the FeedBelt");
        !fixMalfunctionRunning.

+!fixMalfunctionRunning
    <-  .wait(6000);
        !changeStateOfRunning(true);
         .send(ert, achieve, fixMalfunctionFeedBelt);
        .print("Fix running problem for FeedBelt");
        !createMalfunctionRunning.

//adding Raw Plate in FeedBelt
+!controlFeedBelt
    : empty(X) & X = true & supply(Y) & Y = false & running(Z) & Z = true
    <-  .print("AddRawPlate in The FeedBelt");
        !addRawPlate;
        .wait(1500);
        !checkSupply;
        !checkFeedBelt;
        !controlFeedBelt.

//transfer raw plate to ert
+!controlFeedBelt
    : request(Z) & Z = true & empty(X) & X = false & running(Z) & Z = true
    <-  .print("Transfer RawPlate into ERT");
        setRequest(false);
        .wait(500);
        .send(ert, achieve, activeERT);
        !checkSupply;
        !checkFeedBelt;
        !controlFeedBelt.

//Simple updating
+!controlFeedBelt
    <-  .wait(1000);
        !checkSupply;
        !checkFeedBelt;
        !controlFeedBelt.

//Sent from ERT
+!doRequest
    <-   setRequest(true).

//Some plans for managing errors
+!changeStateOfRunning(X)
    <-  changeStateOfRunning(X).

+!getToken
    <-  getToken.

+!getTDProperties
    <-  getTDProperties.

+!addRawPlate
    <- addRawPlate.

+!checkSupply
    <- checkSupply.

+!checkFeedBelt
    <- checkFeedBelt.

-!getToken
    <-  .print("Error retrieving token for communication in the [FEEDBELT]");
        .wait(1000);
        !getToken.

-!getTDProperties
    <-  .print("Error retrieving propriety for communication in the [FEEDBELT]");
        .wait(1000);
        !getTDProperties.


-!addRawPlate
    <-  .print("Error adding plate for communication in the [FEEDBELT]");
        .wait(1000);
        !addRawPlate.

-!checkSupply
    <-  .print("Error checking supply for communication in the [FEEDBELT]");
        .wait(1000);
        !checkSupply.

-!checkFeedBelt
    <-  .print("Error checking empty for communication in the [FEEDBELT]");
        .wait(1000);
        !checkFeedBelt.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
