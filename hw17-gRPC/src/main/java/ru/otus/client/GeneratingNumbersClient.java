package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.GeneratingNumbersRequest;
import ru.otus.GeneratingNumbersServiceGrpc;

public class GeneratingNumbersClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final long FIRST_NUMBER = 0;
    private static final long LAST_NUMBER = 30;
    private static final long LAST_CLIENT_NUMBER = 30;
    private static long currentValue = 0;
    private static final Logger log = LoggerFactory.getLogger(GeneratingNumbersClient.class);

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var service = GeneratingNumbersServiceGrpc.newStub(channel);
        var generatingNumbersRequest = GeneratingNumbersRequest.newBuilder()
                .setFirstValue(FIRST_NUMBER)
                .setLastValue(LAST_NUMBER)
                .build();
        GeneratingNumbersClientStreamObserver observer = new GeneratingNumbersClientStreamObserver();
        service.getNumbers(generatingNumbersRequest, observer);
        for (var i = FIRST_NUMBER; i < LAST_CLIENT_NUMBER; i++) {
            currentValue = currentValue + observer.getLastNumber() + 1;
            log.info("currentValue: {}", currentValue);
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        channel.shutdown();
    }
}
