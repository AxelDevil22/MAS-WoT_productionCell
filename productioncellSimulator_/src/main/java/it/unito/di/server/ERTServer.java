package it.unito.di.server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unito.di.productioncell.ControllerProductionCell;


public class ERTServer {

    private  final ControllerProductionCell controllerProductionCell;
    String usernameServer = "ert";
    String passwordServer = "ert";

    static String  token = "11b8f71c-4fb6-4489-8348-e137ab9f1fbg";

    public ERTServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }

    public void startERTServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8003), 0);
        server.createContext("/getToken", new GetTokenHandler());
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/rotate", new RotateHandler());
        server.createContext("/up", new UpHandler());
        server.createContext("/down", new DownHandler());
        server.createContext("/transferTO", new TransferTOHandler());
        server.createContext("/checkAngle", new CheckAngleHandler());
        server.createContext("/checkEmpty", new CheckEmptyHandler());
        server.createContext("/checkPositionY", new CheckPositionYHandler());
        server.createContext("/movementProblem", new MovementProblemHandler());
        System.out.println("Press server started on port " + 8003);
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
                String td = new String(Files.readAllBytes(Paths.get("src/main/java/it/unito/di/server/ertServer.json")));
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, td.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(td.getBytes());
                os.close();
            } else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class RotateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.rotateAnimationERT();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }
            else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class UpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.upAnimationERT();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
             }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class DownHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.downAnimationERT();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class CheckEmptyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                String response = String.valueOf(controllerProductionCell.checkEmptyERT());
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                 exchange.sendResponseHeaders(401, 0);
        }
    }
    class TransferTOHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                controllerProductionCell.transferTOERT();
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }

    }
    class CheckAngleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                float angle = controllerProductionCell.checkAngleERT();
                String response = String.valueOf(angle);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }

    class CheckPositionYHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String tokenTotest = exchange.getRequestHeaders().getFirst("Token");
            if (token.equals(tokenTotest)) {
                int position = controllerProductionCell.checkPositionERT();
                String response = String.valueOf(position);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
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
                 controllerProductionCell.changeColorErt(condition);
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
            }else
                exchange.sendResponseHeaders(401, 0);
        }
    }
}



