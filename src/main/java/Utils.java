import com.google.protobuf.Timestamp;

import java.util.Date;

/**
 * Created by liza on 13.12.16.
 */
public class Utils {
    public static InstantMessenger.Message craftMessage(String text, String name) {
        return InstantMessenger.Message.newBuilder()
                .setName(name)
                .setTime(Timestamp.newBuilder().setSeconds(new Date().getTime() / 1000))
                .setContent(text)
                .build();
    }
}
