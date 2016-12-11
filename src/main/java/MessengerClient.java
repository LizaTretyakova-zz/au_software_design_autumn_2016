import java.util.Date;

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
//    private final Sender sender;
//    private final Receiver receiver;
//    private final List<BaseMessage> history = new ArrayList<BaseMessage>();
//    private final String peer;
//
//    public MessengerClient(Sender sender, Receiver receiver, String peer) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.peer = peer;
//    }
//
//    public BaseMessage Send(String text) {
//        BaseMessage msg = new BaseMessage(peer, text, new Date());
//        sender.Send(msg);
//        history.add(msg);
//        return msg;
//    }
//
//    public BaseMessage Receive() {
//        BaseMessage msg = receiver.Receive();
//        history.add(msg);
//        return msg;
//    }

    private static final Logger logger = Logger.getLogger(MessengerClient.class.getName());

    private final ManagedChannel channel;
    private final MessengerGrpc.MessengerStub asyncStub;
    private final String name;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public MessengerClient(String host, int port, String name) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build();
        asyncStub = MessengerGrpc.newStub(channel);
        this.name = name;
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private InstantMessenger.Message craftMessage(String text) {
        return InstantMessenger.Message.newBuilder()
                .setName(name)
                .setTime(Timestamp.newBuilder().setSeconds(new Date().getTime() / 1000))
                .setContent(text)
                .build();
    }

    /**
     * Bi-directional example, which can only be asynchronous. Send some chat messages, and print any
     * chat messages that are sent from the server.
     */
    public void sendMessage(String text) throws InterruptedException {
//        InstantMessenger.Message msg = craftMessage(text);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<InstantMessenger.Message> requestObserver =
                asyncStub.sendMessage(new StreamObserver<InstantMessenger.Message>() {
                    @Override
                    public void onNext(InstantMessenger.Message msg) {
                        logger.log(Level.INFO, "Got message \"{0}\" at {1}",
                                new Object[]{msg.getContent(), msg.getTime().toString()});
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

        try {
            InstantMessenger.Message[] requests =
                    {craftMessage("First message"), craftMessage("Second message"),
                            craftMessage("Third message"), craftMessage("Fourth message")};

            for (InstantMessenger.Message request : requests) {
                logger.log(Level.INFO, "Sending message \"{0}\" at {1}",
                        new Object[]{request.getContent(), request.getTime().toString()});
                requestObserver.onNext(request);
            }
        } catch (RuntimeException e) {
            // Cancel RPC
            requestObserver.onError(e);
            throw e;
        }
        // Mark the end of requests
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        finishLatch.await(1, TimeUnit.MINUTES);
    }

}
