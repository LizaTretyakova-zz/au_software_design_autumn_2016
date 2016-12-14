import java.io.IOException;
import java.util.Date;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * Created by liza on 07.12.16.
 */
public class MessengerClient {
    private static final Logger logger = Logger.getLogger(MessengerClient.class.getName());

    private final ManagedChannel channel;
    private final MessageQueue messageQueue = new MessageQueue();
    private final String name;

    private final StreamObserver<InstantMessenger.Message> requestObserver;
    private final CountDownLatch finishLatch;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public MessengerClient(String host, int port, String name) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build();
        this.name = name;

        MessengerGrpc.MessengerStub asyncStub = MessengerGrpc.newStub(channel);
        // The value here is exactly one since we want to communicate with the only with the server
        finishLatch = new CountDownLatch(1);
        requestObserver = asyncStub.sendMessage(new StreamObserver<InstantMessenger.Message>() {
            @Override
            public void onNext(InstantMessenger.Message msg) {
                logger.log(Level.INFO, "[CLIENT] Got a message.");
                messageQueue.store(msg);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                logger.log(Level.WARNING, "RouteChat Failed: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.log(Level.INFO, "Finished Messenger");
                finishLatch.countDown();
            }
        });
    }

    public void shutdown() throws InterruptedException {
        // Mark the end of requests
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        finishLatch.await(1, TimeUnit.MINUTES);

        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);

        logger.log(Level.INFO, "[CLIENT] It's over now.");
    }

    public void sendMessage(String text) throws InterruptedException {
        InstantMessenger.Message msg = Utils.craftMessage(text, name);
        requestObserver.onNext(msg);
    }
}
