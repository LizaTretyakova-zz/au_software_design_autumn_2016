package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liza on 11.12.16.
 */
public class MessageQueue {

    private List<InstantMessenger.Message> queue = new ArrayList<>();

    public void store(InstantMessenger.Message msg) {
        queue.add(msg);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
