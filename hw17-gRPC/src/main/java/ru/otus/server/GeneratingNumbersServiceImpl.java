package ru.otus.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.GeneratingNumbersRequest;
import ru.otus.GeneratingNumbersResponse;
import ru.otus.GeneratingNumbersServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GeneratingNumbersServiceImpl extends GeneratingNumbersServiceGrpc.GeneratingNumbersServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(GeneratingNumbersServiceImpl.class);

    @Override
    public void getNumbers(GeneratingNumbersRequest request, StreamObserver<GeneratingNumbersResponse> responseObserver) {
        log.info("generate numbers from {} to {}", request.getFirstValue(), request.getLastValue());
        AtomicLong currentNumber = new AtomicLong(request.getFirstValue());
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(
                () -> {
                    long value = currentNumber.incrementAndGet();
                    responseObserver.onNext(GeneratingNumbersResponse.newBuilder().setNumber(value).build());
                    if (currentNumber.get() > request.getLastValue()) {
                        executor.shutdown();
                        responseObserver.onCompleted();
                        log.info("generate ends");
                    }
                },
                0,
                2,
                TimeUnit.SECONDS);
    }

}
