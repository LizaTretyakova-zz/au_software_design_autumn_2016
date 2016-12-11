import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by liza on 11.12.16.
 */
public class MessengerServer {

    private final MessageQueue messageQueue = new MessageQueue();

    private class MessengerImpl extends MessengerGrpc.MessengerImplBase {

        private final Logger logger = Logger.getLogger(MessengerImpl.class.getName());

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

            return new StreamObserver<InstantMessenger.Message>() {
                @Override
                public void onNext(InstantMessenger.Message msg) {
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
