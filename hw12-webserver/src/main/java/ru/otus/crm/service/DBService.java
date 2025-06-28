package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с сущностями в БД
 */
public interface DBService<T> {

    T save(T object);

    Optional<T> get(long id);

    Optional<T> getFirstByField(String field, String value);

    List<T> findAll();

}
