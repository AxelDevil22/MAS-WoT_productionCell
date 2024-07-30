package it.unito.di.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import it.unito.di.press.PressException;
import it.unito.di.productioncell.ControllerProductionCell;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetSocketAddress;

public class StartServer {
    private  final ControllerProductionCell controllerProductionCell;
    int port = 8001;

    public StartServer(ControllerProductionCell controllerProductionCell) {
        this.controllerProductionCell = controllerProductionCell;
    }
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/td", new ThingDescriptionHandler());
        server.createContext("/activateCellProduction", new ActivateCellProductionHandler());
        server.createContext("/activateAddSupply", new ActivateAddSupplyHandler());
        System.out.println("Press server started on port " + 8001);
        server.start();
    }
    static class ThingDescriptionHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String td = readThingDescriptionFromFile("src/main/java/it/unito/di/server/startServer.json");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, td.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(td.getBytes());
            os.close();
        }
    }
    class ActivateCellProductionHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try {
                controllerProductionCell.activateCellProduction();
            } catch (PressException e) {
                throw new RuntimeException(e);
            }
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }
    }

     class ActivateAddSupplyHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            controllerProductionCell.activateAddSupply();
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }
    }

    public static String readThingDescriptionFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}

