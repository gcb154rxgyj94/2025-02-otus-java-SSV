package ru.otus.core.sessionmanager;

/**
 * Интерфейс менеджеров транзакций
 */
public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);

    <T> T doInReadOnlyTransaction(TransactionAction<T> action);
}
