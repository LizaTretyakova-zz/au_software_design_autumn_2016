package Model;

import Controller.Controller;
import com.google.protobuf.Timestamp;

import java.util.Date;

/**
 * Created by liza on 13.12.16.
 * Base class for messenger implementations.
 */
public abstract class Messenger {

    protected final MessageQueue messageQueue;
    protected final String name;

    protected Messenger(String name, Controller controller) {
        this.name = name;
        messageQueue = new MessageQueue(controller);
    }

    public abstract void sendMessage(String text);

    public boolean hasSomethingToSend() {
        return !messageQueue.isEmpty();
    }

    public void waitConnection() {}

    public void shutdown() throws InterruptedException {}

    public static InstantMessenger.Message craftMessage(String text, String name) {
        return InstantMessenger.Message.newBuilder()
                .setName(name)
                .setTime(Timestamp.newBuilder().setSeconds(new Date().getTime() / 1000))
                .setContent(text)
                .build();
    }
}
