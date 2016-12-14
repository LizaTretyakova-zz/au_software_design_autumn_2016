import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerTest {

    @Test
    public void test() throws Exception {
        MessengerServer server = new MessengerServer(8982, "RedLightingSpot");
        MessengerClient client = new MessengerClient("localhost", 8982, "JohnyCatswill");
        server.waitConnection();
        server.sendMessage("Johny?");
        client.sendMessage("Meow");
        server.sendMessage("Good cat!");
        client.sendMessage("MeowMeow");
        client.sendMessage("MeowMeowMeow");
        server.sendMessage("Buy!");
        client.shutdown();
    }
}