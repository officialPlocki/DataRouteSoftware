package de.plocki.client.panel;

import de.plocki.Main;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;

public class ClientPanel {

    private final JPanel panel;

    public ClientPanel() {
        panel = new JPanel();
    }

    public JPanel getPanel() {
        JLabel label = new JLabel("Upload file");
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton uploadButton = new JButton("Select File");
        uploadButton.setFont(new Font("Arial", Font.PLAIN, 20));
        uploadButton.addActionListener(e -> {
            System.out.println("opening file chooser");
            JFileChooser chooser = new JFileChooser();
            int val = chooser.showOpenDialog(null);
            if(val == JFileChooser.APPROVE_OPTION) {
                System.out.println("uploading file");
                try {
                    System.out.println("connecting");
                    URLConnection connection = new URL(Main.getClient().getConfig().get("serverConfig").getString("address") + "/files?type=upload&key=" + Main.getClient().getConfig().get("serverConfig").getString("privateKey") + "&format=" + FilenameUtils.getExtension( chooser.getSelectedFile().getName())).openConnection();
                    connection.setDoOutput(true);
                    System.out.println("connected");
                    OutputStream os = connection.getOutputStream();
                    System.out.println("uploading");
                    byte[] bytes = Files.readAllBytes(chooser.getSelectedFile().getAbsoluteFile().toPath());

                    String str = Base64.getEncoder().encodeToString(bytes);

                    os.write(str.getBytes());
                    os.flush();
                    os.close();

                    System.out.println("uploaded");


                    System.out.println("awaiting response");
                    InputStream is = connection.getInputStream();
                    Scanner scanner = new Scanner(is);
                    String response = scanner.nextLine();

                    String url = Main.getClient().getConfig().get("serverConfig").getString("address") + response;

                    System.out.println("response");
                    System.out.println(url);
                    JFrame frame = new JFrame();
                    frame.setSize(Main.getClient().getConfig().get("urlPanel").getInt("width"), Main.getClient().getConfig().get("urlPanel").getInt("height"));

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(3, 1));
                    JLabel l = new JLabel("File uploaded");
                    l.setFont(new Font("Arial", Font.PLAIN, 20));
                    panel.add(l);

                    JButton copy = new JButton("Copy Link");
                    copy.setFont(new Font("Arial", Font.PLAIN, 20));
                    copy.addActionListener(e2 -> {
                        frame.dispose();
                        System.out.println("copied link");
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
                    });
                    panel.add(copy);

                    JButton open = new JButton("Open Link");
                    open.setFont(new Font("Arial", Font.PLAIN, 20));
                    open.addActionListener(e2 -> {
                        System.out.println("opening link");
                        frame.dispose();
                        try {
                            Desktop.getDesktop().browse(new URL(url).toURI());
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    });
                    panel.add(open);
                    frame.add(panel);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(label);
        panel.add(uploadButton);
        panel.setLayout(new GridLayout(2,1));
        return panel;
    }

}
