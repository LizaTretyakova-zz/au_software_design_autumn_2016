import java.util.Date;

/**
 * Created by liza on 07.12.16.
 */
public class BaseMessage {
    String destName;
    String text;
    Date time;

    public BaseMessage(String name, String msg, Date time) {
        this.destName = name;
        this.text = msg;
        this.time = time;
    }
}
