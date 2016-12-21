package Model;

import java.util.ArrayList;
import java.util.List;
import Controller.Controller;

/**
 * Created by liza on 11.12.16.
 * Allow messenger not to bother about storing messages and showing to users.
 */
public class MessageQueue {

    private final Controller controller;

    public MessageQueue(Controller controller) {
        this.controller = controller;
    }

    private List<InstantMessenger.Message> queue = new ArrayList<>();

    public void store(InstantMessenger.Message msg) {
        queue.add(msg);
        controller.drawMessage(controller.prepareText(msg.getName(), msg.getContent()));
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
