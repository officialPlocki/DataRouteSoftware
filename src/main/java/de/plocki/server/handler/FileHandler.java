package de.plocki.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.plocki.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }

        if(result.get("type").equals("file")) {
            if(result.containsKey("fileKey")) {
                if(result.containsKey("format")) {
                    String fileKey = result.get("fileKey");
                    File f = new File("files/" + fileKey + "." + result.get("format"));
                    if(f.exists()) {
                        exchange.sendResponseHeaders(200, f.length());
                        OutputStream os = exchange.getResponseBody();
                        Files.copy(f.toPath(), os);
                        exchange.getResponseBody().flush();
                        exchange.getResponseBody().close();
                        exchange.close();
                    } else {
                        String bytes = "File not found";
                        exchange.sendResponseHeaders(404, bytes.getBytes().length);
                        exchange.getResponseBody().write(bytes.getBytes());
                        exchange.getResponseBody().close();
                        exchange.close();
                    }
                } else {
                    String bytes = "Format not found";
                    exchange.sendResponseHeaders(404, bytes.getBytes().length);
                    exchange.getResponseBody().write(bytes.getBytes());
                    exchange.getResponseBody().close();
                    exchange.close();
                }
            } else {
                String bytes = "Key error";
                exchange.sendResponseHeaders(404, bytes.getBytes().length);
                exchange.getResponseBody().write(bytes.getBytes());
                exchange.getResponseBody().close();
                exchange.close();
            }
        } else if(result.get("type").equals("upload")) {
            if(result.containsKey("key")) {
                if(result.get("key").equals(Main.getServer().getConfig().get("privateKey").getString("value"))) {
                    if(result.containsKey("format")) {
                        byte[] bytes = new byte[exchange.getRequestBody().available()];
                        exchange.getRequestBody().read(bytes);
                        String uuid = UUID.randomUUID().toString();
                        File file = new File("files" + File.separator + uuid + "." + result.get("format"));
                        Files.copy(exchange.getRequestBody(), file.toPath());
                        String b = "/files?fileKey=" + uuid + "&format=" + result.get("format");
                        exchange.sendResponseHeaders(200, b.getBytes().length);
                        exchange.getResponseBody().write(b.getBytes());
                        exchange.getResponseBody().close();
                        exchange.close();
                    } else {
                        String bytes = "Missing format";
                        exchange.sendResponseHeaders(404, bytes.getBytes().length);
                        exchange.getResponseBody().write(bytes.getBytes());
                        exchange.getResponseBody().close();
                        exchange.close();
                    }
                } else {
                    String bytes = "Key error";
                    exchange.sendResponseHeaders(404, bytes.getBytes().length);
                    exchange.getResponseBody().write(bytes.getBytes());
                    exchange.getResponseBody().close();
                    exchange.close();
                }
            } else {
                String bytes = "Key error";
                exchange.sendResponseHeaders(404, bytes.getBytes().length);
                exchange.getResponseBody().write(bytes.getBytes());
                exchange.getResponseBody().close();
                exchange.close();
            }
        } else {
            String bytes = "Query error";
            exchange.sendResponseHeaders(404, bytes.getBytes().length);
            exchange.getResponseBody().write(bytes.getBytes());
            exchange.getResponseBody().close();
            exchange.close();
        }
        String bytes = "No fitting query";
        exchange.sendResponseHeaders(404, bytes.getBytes().length);
        exchange.getResponseBody().write(bytes.getBytes());
        exchange.getResponseBody().close();
        exchange.close();
    }

}
