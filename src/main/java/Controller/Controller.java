package Controller;

import Model.Messenger;
import Model.MessengerClient;
import Model.MessengerServer;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by liza on 16.12.16.
 */
public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private Messenger messenger;

    public Controller() {
        this.messenger = null;
    }

    public void createConnection(boolean isHost, String addr, int port, String name) throws IOException {
        if(isHost) {
            messenger = new MessengerServer(port, name);
            logger.info("Created server");
            messenger.waitConnection();
            logger.info("Connected (server)");
        } else {
            messenger = new MessengerClient(addr, port, name);
            logger.info("Created client");
            logger.info("Connected (client)");
        }
    }

    public void sendMessage(String text) {
        messenger.sendMessage(text);
    }

    public void disconnect() throws InterruptedException {
        messenger.shutdown();
    }
}
