import Controller.Controller;
import Model.MessengerClient;
import Model.MessengerServer;
import org.junit.Test;
import GUI.GUI;

import java.util.concurrent.*;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerTest {

//    @Test
//    public void test() throws Exception {
//        MessengerServer server = new MessengerServer(8982, "RedLightingSpot");
//        MessengerClient client = new MessengerClient("localhost", 8982, "JohnyCatswill");
//        server.waitConnection();
//        server.sendMessage("Johny?");
//        client.sendMessage("Meow");
//        server.sendMessage("Good cat!");
//        client.sendMessage("MeowMeow");
//        client.sendMessage("MeowMeowMeow");
//        server.sendMessage("Buy!");
//        client.shutdown();
//    }
    @Test
    public void testMain() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        Runnable task = () -> {
            GUI gui = new GUI();
            Controller controller = new Controller();
            gui.init(controller);
            System.out.println("meow");
        };

        pool.execute(task);
        pool.execute(task);
        pool.shutdown();
        pool.awaitTermination(1000, TimeUnit.DAYS);
    }

}