package ru.otus.crm.service;

/**
 * Фабрика сервисов БД
 */
public interface ServiceFactory {

    <T> DBService getService(Class<T> entityClass);

}
