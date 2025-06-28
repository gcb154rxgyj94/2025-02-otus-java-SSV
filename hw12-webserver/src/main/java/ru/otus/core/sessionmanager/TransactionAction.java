package ru.otus.core.sessionmanager;

import org.hibernate.Session;

import java.util.function.Function;

/**
 * Тарнзакция
 */
public interface TransactionAction<T> extends Function<Session, T> {}
