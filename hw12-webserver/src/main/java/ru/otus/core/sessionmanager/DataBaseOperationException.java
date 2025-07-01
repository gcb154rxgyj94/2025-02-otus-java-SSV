package ru.otus.core.sessionmanager;

/**
 * Исключение при работе с БД
 */
public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
