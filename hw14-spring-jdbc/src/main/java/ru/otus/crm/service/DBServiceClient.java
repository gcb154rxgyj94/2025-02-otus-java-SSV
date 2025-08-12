package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;

/**
 * Сервис для работы с сущностями в БД
 */
public interface DBServiceClient {

    Client saveClient(Client object);

    List<Client> findAll();

}
