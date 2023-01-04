package de.plocki.client;

import de.plocki.client.panel.ClientPanel;
import de.plocki.util.JSONFile;
import de.plocki.util.JSONValue;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.util.UUID;

public class ClientMain {

    private final JSONFile config;

    public ClientMain() throws IOException {
        config = new JSONFile("client.json",
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "serverConfig";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("address", "http://localhost:4993")
                                .put("privateKey", UUID.randomUUID().toString());
                    }
                },
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "uploadPanelConfig";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("width", 800)
                                .put("height", 600);
                    }
                },
                new JSONValue() {
                    @Override
                    public String objectName() {
                        return "urlPanel";
                    }

                    @Override
                    public JSONObject object() {
                        return new JSONObject()
                                .put("width", 800)
                                .put("height", 600);
                    }
                }
        );
    }

    public JSONFile getConfig() {
        return config;
    }

    public void start() {
        JFrame frame = new JFrame();
        frame.setSize(config.get("uploadPanelConfig").getInt("width"), config.get("uploadPanelConfig").getInt("height"));
        frame.add(new ClientPanel().getPanel());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
