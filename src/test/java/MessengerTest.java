import org.junit.Test;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerTest {

    @Test
    public void test() throws Exception {
//        MessengerServer.main(null);
//        MessengerClient.testRun(null);
//        System.exit(0);

        MessengerServer server = new MessengerServer(8980, "RedLightingSpot");
//        server.sendMessage("Johny?");

        MessengerClient client = new MessengerClient("localhost", 8980, "JohnyCatswill");
        client.sendMessage("Meow");
        // server.sendMessage("Johny?");
        // server.sendMessage("Good cat!");
        client.sendMessage("MeowMeow");
        client.sendMessage("MeowMeowMeow");
        // server.sendMessage("Buy!");
        client.shutdown();
    }
}