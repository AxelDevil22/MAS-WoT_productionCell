package it.unito.di.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unito.di.productioncell.ControllerProductionCell;

public class PressServer {

    private  final ControllerProductionCell controllerProductionCell;
    String usernameServer = "press";
    String passwordServer = "press";

    static String  token = "11b8f71c-4fb6-4489-8348-e137ab9f1fbf";
    public PressServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }

    public void startPressServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8005), 0);
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/getToken", new GetTokenHandler());
        server.createContext("/checkEmpty", new CheckEmptyHandler());
        server.createContext("/checkPositionX", new CheckPositionXHandler());
        server.createContext("/checkForging", new CheckForgingHandler());
        server.createContext("/checkIsForged", new CheckIsForgedHandler());
        server.createContext("/forgePlate", new ForgePlateHandler());
        server.createContext("/openPress", new OpenPressHandler());
        server.createContext("/closePress", new ClosePressHandler());
        server.createContext("/movementProblem", new MovementProblemHandler());
        System.out.println("Press server started on port " + 8005);
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
                exchange.sendResponseHeaders(401, -1); // Unauthorized
            }
        }
    }
    static class ThingDescriptionHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String td = new String(Files.readAllBytes(Paths.get("src/main/java/it/unito/di/server/pressServer.json")));
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, td.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(td.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

     class CheckEmptyHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.pressIsEmpty());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

     class CheckPositionXHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                int positionX = controllerProductionCell.checkPositionXPress();
                String response = String.valueOf(positionX);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

     class CheckForgingHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.pressIsForging());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

     class CheckIsForgedHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.checkPressIsForged());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

     class ForgePlateHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.forgePressAnimation();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

     class OpenPressHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.openPressAnimation();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
             }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

     class ClosePressHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.closePressAnimation();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }

    class MovementProblemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean condition = Boolean.parseBoolean(exchange.getRequestHeaders().getFirst("Condition"));
                controllerProductionCell.changeColorPress(condition);
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }
}
