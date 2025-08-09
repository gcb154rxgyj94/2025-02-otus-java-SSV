package ru.otus.crm.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с Client
 */
@Service
@AllArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            clientRepository.save(client);
            log.info("saved client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientList = clientRepository.findAll();
        log.info("clientList:{}", clientList);
        return clientList;
    }

}
