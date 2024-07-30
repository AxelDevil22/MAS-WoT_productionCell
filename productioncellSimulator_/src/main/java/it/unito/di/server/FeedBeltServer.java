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



public class FeedBeltServer {

    String usernameServer = "feedBelt";
    String passwordServer = "feedBelt";

    static String  token = "11b8f71c-4fb6-4489-8348-e137ab9f1fbf";
    private  final ControllerProductionCell controllerProductionCell;

    public FeedBeltServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }

    public void startFeedBeltServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
        server.createContext("/getToken", new GetTokenHandler());
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/checkSupply", new CheckSupplyHandler());
        server.createContext("/checkFeedBelt", new CheckFeedBeltHandler());
        server.createContext("/addRawPlate", new AddRawPlateHandler());
        server.createContext("/changeStateOfRunning", new changeStateOfRunningHandelr());
        System.out.println("Press server started on port " + 8002);
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
                String td = new String(Files.readAllBytes(Paths.get("src/main/java/it/unito/di/server/feedBeltServer.json")));
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, td.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(td.getBytes());
                os.close();
            } else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class CheckSupplyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean empty = controllerProductionCell.checkSupply();
                String response = String.valueOf(empty);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            else
                exchange.sendResponseHeaders(401,0);

        }

    }

    class CheckFeedBeltHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean empty = controllerProductionCell.checkFeedBelt();
                String response = String.valueOf(empty);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            }else
                exchange.sendResponseHeaders(401,0);
        }
    }

    class AddRawPlateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.addMaterials();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }
            else
                exchange.sendResponseHeaders(401,0);
        }
    }

    class changeStateOfRunningHandelr implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                boolean running = Boolean.parseBoolean(exchange.getRequestHeaders().getFirst("Condition"));
                controllerProductionCell.changeRunningFeedBelt(running);
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }
            else
                exchange.sendResponseHeaders(401,0);
        }
    }

}

