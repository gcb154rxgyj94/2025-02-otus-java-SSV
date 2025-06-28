package ru.otus.webserver;

/**
 * Веб-сервер клиента
 */
public interface ClientsWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
