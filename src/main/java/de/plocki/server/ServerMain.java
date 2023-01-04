package de.plocki.server;

import com.sun.net.httpserver.HttpServer;
import de.plocki.server.handler.FileHandler;
import de.plocki.util.JSONFile;
import de.plocki.util.JSONValue;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class ServerMain {

    private final JSONFile config;

    public ServerMain() throws IOException {
        config = new JSONFile("server.json",
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "privateKey";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("value", UUID.randomUUID().toString());
                    }
                },
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "serverConfig";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("port", 4993);
                    }
                }
        );
    }

    public JSONFile getConfig() {
        return config;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(config.get("serverConfig").getInt("port")), 0);
        server.createContext("/files", new FileHandler());
        server.start();
        File file = new File("files");
        if(!file.exists()) {
            file.mkdir();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(0);
        }));
    }

}
