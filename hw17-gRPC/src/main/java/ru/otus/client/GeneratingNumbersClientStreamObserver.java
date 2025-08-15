package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.GeneratingNumbersResponse;

public class GeneratingNumbersClientStreamObserver implements StreamObserver<GeneratingNumbersResponse> {

    private static final Logger log = LoggerFactory.getLogger(GeneratingNumbersClientStreamObserver.class);
    private long lastNumber = 0;

    @Override
    public void onNext(GeneratingNumbersResponse generatingNumbersResponse) {
        long lastNumber = generatingNumbersResponse.getNumber();
        log.info("server value: {}", lastNumber);
        setLastNumber(lastNumber);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("server error", throwable);
    }

    @Override
    public void onCompleted() {
        log.info("server completed");
    }

    public long getLastNumber() {
        long temp = lastNumber;
        setLastNumber(0);
        return temp;
    }

    private synchronized void setLastNumber(long number) {
        this.lastNumber = number;
    }

}
