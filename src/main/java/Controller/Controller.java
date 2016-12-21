package Controller;

import Model.Messenger;
import Model.MessengerClient;
import Model.MessengerServer;

import GUI.GUI;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by liza on 16.12.16.
 * Allows connection between GUI and core.
 */
public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private Messenger messenger;
    private GUI gui;

    public Controller(GUI gui) {
        this.messenger = null;
        this.gui = gui;
    }

    public void createConnection(boolean isHost, String addr, int port, String name) throws IOException {
        if(isHost) {
            messenger = new MessengerServer(port, name, this);
            logger.info("Created server");
            messenger.waitConnection();
            logger.info("Connected (server)");
        } else {
            messenger = new MessengerClient(addr, port, name, this);
            logger.info("Created client");
            logger.info("Connected (client)");
        }
    }

    public void sendMessage(String text) {
        messenger.sendMessage(text);
    }

    public void drawMessage(String text) {
        gui.showMessage(text);
    }

    public String prepareText(String name, String text) {
        return name + " said:\n" + text + "\n\n";
    }

    public void disconnect() throws InterruptedException {
        messenger.shutdown();
    }
}
