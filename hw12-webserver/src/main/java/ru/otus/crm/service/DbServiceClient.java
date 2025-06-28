package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с Client
 */
public class DbServiceClient implements DBService<Client> {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClient.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> clientsCache;

    public DbServiceClient(TransactionManager transactionManager,
                               DataTemplate<Client> clientDataTemplate,
                               HwCache<String, Client> clientsCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.clientsCache = clientsCache;
    }

    @Override
    public Client save(Client client) {
        Client client1 = transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
        clientsCache.put(getKey(client1.getId()), client1.clone());
        return client1;
    }

    @Override
    public Optional<Client> get(long id) {
        Optional<Client> client = Optional.of(clientsCache.get(getKey(id)));
        if (client.isPresent()) {
            return client;
        }
        client = transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
        client.ifPresent(value -> clientsCache.put(getKey(value.getId()), value.clone()));
        return client;
    }

    @Override
    public Optional<Client> getFirstByField(String field, String value) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findByEntityField(session, field, value);
            Optional<Client> optionalClient = clientList.isEmpty() ? Optional.empty() : Optional.of(clientList.get(0));
            log.info("user: {}", optionalClient);
            return optionalClient;
        });
    }

    @Override
    public List<Client> findAll() {
        List<Client> list = transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
        list.forEach(value -> clientsCache.put(getKey(value.getId()), value.clone()));
        return list;
    }

    private String getKey(long id) {
        return String.valueOf(id);
    }

}
