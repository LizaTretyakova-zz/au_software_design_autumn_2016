package Controller;

import Model.Messenger;
import Model.MessengerClient;
import Model.MessengerServer;

import java.io.IOException;

/**
 * Created by liza on 16.12.16.
 */
public class Controller {

    private Messenger messenger;

    public Controller() {
        this.messenger = null;
    }

    public void createConnection(boolean isHost, String addr, int port, String name) throws IOException {
        if(isHost) {
            messenger = new MessengerServer(port, name);
            messenger.waitConnection();
        } else {
            messenger = new MessengerClient(addr, port, name);
        }
    }

    public void sendMessage(String text) {
        messenger.sendMessage(text);
    }

    public void disconnect() throws InterruptedException {
        messenger.shutdown();
    }
}
