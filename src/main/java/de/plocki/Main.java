package de.plocki;

import de.plocki.client.ClientMain;
import de.plocki.server.ServerMain;
import de.plocki.util.JSONFile;
import de.plocki.util.JSONValue;
import org.json.JSONObject;

import java.io.IOException;

public class Main {

    //@todo fix file upload

    private static ServerMain server;
    private static ClientMain client;

    public static void main(String[] args) throws IOException {
        JSONFile file = new JSONFile("type.json",
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "type";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("value", "client");
                    }
                }
        );
        String type = file.get("type").getString("value");
        if (type.equals("client")) {
            client = new ClientMain();
            client.start();
        } else if (type.equals("server")) {
            server = new ServerMain();
            server.start();
        }
    }

    public static ClientMain getClient() {
        return client;
    }

    public static ServerMain getServer() {
        return server;
    }
}