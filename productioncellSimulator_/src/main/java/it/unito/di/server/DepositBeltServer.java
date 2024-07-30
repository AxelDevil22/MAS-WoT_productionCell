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
import java.util.Scanner;

public class DepositBeltServer {

    String usernameServer = "depositBelt";
    String passwordServer = "depositBelt";

    static String  token = "11b8f71c-4fb6-4489-8348-e137ab9f1fbf";
    private final ControllerProductionCell controllerProductionCell;

    public DepositBeltServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }


    public void startDepositBeltServer()throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8006), 0);
        server.createContext("/getToken", new GetTokenHandler());
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/checkDepositBelt", new CheckDepositBeltHandler());
        server.createContext("/storeMetalPlate", new StoreMetalPlateHandler());
        server.createContext("/changeStateOfRunning", new changeStateOfRunningHandelr());
        System.out.println("Server started on port " + 8006);
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
                String td = new String(Files.readAllBytes(Paths.get("src/main/java/it/unito/di/server/depositBeltServer.json")));
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, td.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(td.getBytes());
                os.close();
            } else
                exchange.sendResponseHeaders(401, -1);
        }
    }

    class CheckDepositBeltHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.checkDepositBelt());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, -1);
        }
    }

    class StoreMetalPlateHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.storeMetalPlateAnimation();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            } else
                exchange.sendResponseHeaders(401, -1);
        }
    }

    class changeStateOfRunningHandelr implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean running = Boolean.parseBoolean(exchange.getRequestHeaders().getFirst("Condition"));
                controllerProductionCell.changeRunningDepositBelt(running);
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }
            else
                exchange.sendResponseHeaders(401,0);
        }
    }

}

