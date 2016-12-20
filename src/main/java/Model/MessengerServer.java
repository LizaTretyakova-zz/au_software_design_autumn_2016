package Model;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerServer extends Messenger {

    private static final Logger logger = Logger.getLogger(MessengerServer.class.getName());
    
    private final int port;
    private final Server server;

    private final CompletableFuture<StreamObserver<InstantMessenger.Message>> responseObserverGetter;
    protected StreamObserver<InstantMessenger.Message> responseObserver;

    /** Create a Message server listening on {@code port} */
    public MessengerServer(int port, String name) throws IOException {
        this(ServerBuilder.forPort(port), port, name);
    }

    /** Create a RouteGuide server using serverBuilder as a base and features as data. */
    public MessengerServer(ServerBuilder<?> serverBuilder, int port, String name) throws IOException {
        super(name);

        this.port = port;
        responseObserverGetter = new CompletableFuture<>();
        server = serverBuilder.addService(new MessengerService()).build();

        start();
    }

    @Override
    public void sendMessage(String text) {
        responseObserver.onNext(craftMessage(text, name));
    }

    /**
     * Blocks until a client comes.
     */
    @Override
    public void waitConnection() {
        try {
            logger.log(Level.INFO, "[SERVER] Obtaining responseObserver");
            responseObserver = responseObserverGetter.get();
            logger.log(Level.INFO, "[SERVER] Obtained responseObserver");
        } catch (InterruptedException | ExecutionException e) {
            // Should never happen
            throw new RuntimeException(e);
        }
    }

    /** Start serving requests. */
    private void start() throws IOException {
        server.start();
        logger.info("[SERVER] Started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                MessengerServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        stop();
    }

    private class MessengerService extends MessengerGrpc.MessengerImplBase {

        private final Logger logger = Logger.getLogger(MessengerService.class.getName());
        
        /**
         * Receives a stream of message/location pairs, and responds with a stream of all previous
         * messages at each of those locations.
         *
         * @param responseObserver an observer to receive the stream of previous messages.
         * @return an observer to handle requested message/location pairs.
         */
        @Override
        public StreamObserver<InstantMessenger.Message> sendMessage(
                StreamObserver<InstantMessenger.Message> responseObserver) {

            logger.log(Level.INFO, "OK");
            responseObserverGetter.complete(responseObserver);
            return new StreamObserver<InstantMessenger.Message>() {
                @Override
                public void onNext(InstantMessenger.Message msg) {
                    logger.log(Level.INFO, "[SERVER] Got a message.");
                    messageQueue.store(msg);
                }

                @Override
                public void onError(Throwable t) {
                    logger.log(Level.WARNING, "MessengerImpl: cancelled");
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }

    }
}
