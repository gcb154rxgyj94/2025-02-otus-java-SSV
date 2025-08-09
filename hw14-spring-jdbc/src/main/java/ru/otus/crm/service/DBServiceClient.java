package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с сущностями в БД
 */
public interface DBServiceClient {

    Client saveClient(Client object);

    Optional<Client> getClient(long id);

    List<Client> findAll();

}
