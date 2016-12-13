import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerServer {

    private static final Logger logger = Logger.getLogger(MessengerServer.class.getName());
    
    private final MessageQueue messageQueue = new MessageQueue();
    private final int port;
    private final Server server;
    private final String name;

    protected StreamObserver<InstantMessenger.Message> responseObserver = null;

    /** Create a Message server listening on {@code port} */
    public MessengerServer(int port, String name) throws IOException {
        this(ServerBuilder.forPort(port), port, name);
    }

    /** Create a RouteGuide server using serverBuilder as a base and features as data. */
    public MessengerServer(ServerBuilder<?> serverBuilder, int port, String name) throws IOException {
        this.port = port;
        this.name = name;
        server = serverBuilder.addService(new MessengerService()).build();

        start();
    }

    public void sendMessage(String text) throws IOException {
        if(responseObserver == null) {
            throw new IOException("No client provided.");
        }

        responseObserver.onNext(Utils.craftMessage(text, name));
    }

    /** Start serving requests. */
    private void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                MessengerServer.this.shutdown();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    private void shutdown() {
        if (server != null) {
            server.shutdown();
            responseObserver = null;
        }
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

            MessengerServer.this.responseObserver = responseObserver;
            logger.log(Level.INFO, "OK");
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
