package ru.otus.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GeneratingNumbersServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new GeneratingNumbersServiceImpl())
                .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
