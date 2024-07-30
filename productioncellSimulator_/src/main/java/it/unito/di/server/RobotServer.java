package it.unito.di.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unito.di.productioncell.ControllerProductionCell;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RobotServer {

    private final ControllerProductionCell controllerProductionCell;
    String usernameServer = "robot";
    String passwordServer = "robot";

    static String  token = "11b8f71c-4fb6-4489-8348-e137ab9f1fbb";
    public RobotServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }


    public void startRobotServer()throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8004), 0);
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/getToken", new GetTokenHandler());
        server.createContext("/checkEmptyArm1", new CheckEmptyArm1Handler());
        server.createContext("/checkEmptyArm2", new CheckEmptyArm2Handler());
        server.createContext("/checkAngle", new CheckAngleHandler());
        server.createContext("/rotateUp", new RotateUpHandler());
        server.createContext("/rotateDown", new RotateDownHandler());
        server.createContext("/activeMagnet1", new ActiveMagnet1Handler());
        server.createContext("/activeMagnet2", new ActiveMagnet2Handler());
        server.createContext("/movementProblem", new MovementProblemHandler());
        System.out.println("Server started on port " + 8004);
        server.start();
    }

    class GetTokenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String usernameTotest = exchange.getRequestHeaders().getFirst("Username");
            String passwordTotest = exchange.getRequestHeaders().getFirst("Password");

            if (usernameTotest.equals(usernameServer) && passwordTotest.equals(passwordServer)) {
                String response = token;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(401, -1);
            }
        }
    }

    static class ThingDescriptionHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String td = new String(Files.readAllBytes(Paths.get("src/main/java/it/unito/di/server/robotServer.json")));
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, td.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(td.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }


    class CheckEmptyArm1Handler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.checkArm1EMpty());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

     class CheckEmptyArm2Handler implements HttpHandler {
         public void handle(HttpExchange exchange) throws IOException {
             String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
             if (token.equals(tokenTotest)) {
                 String response = String.valueOf(controllerProductionCell.checkArm2EMpty());
                 exchange.sendResponseHeaders(200, response.length());
                 OutputStream os = exchange.getResponseBody();
                 os.write(response.getBytes());
                 os.close();

             }else
                 exchange.sendResponseHeaders(401,0);
        }
     }

     class CheckAngleHandler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                float angle = controllerProductionCell.checkAngleRobot();
                String response = String.valueOf(angle);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401,0);
        }
    }

     class RotateUpHandler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.rotateUpRobot();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401,0);
        }
    }

     class RotateDownHandler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.rotateDownRobot();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401,0);
        }
    }

     class ActiveMagnet1Handler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.activeMagnetOne();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401,0);
        }
    }

     class ActiveMagnet2Handler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.activeMagnetTwo();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401,0);
        }
    }

    class MovementProblemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean condition = Boolean.parseBoolean(exchange.getRequestHeaders().getFirst("Condition"));
                controllerProductionCell.changeColorRobot(condition);
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }
}
