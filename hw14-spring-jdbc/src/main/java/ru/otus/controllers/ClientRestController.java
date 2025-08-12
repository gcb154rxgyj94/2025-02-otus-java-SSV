package ru.otus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;

/**
 * Controller для Client
 */
@RestController
@AllArgsConstructor
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return dbServiceClient.saveClient(client);
    }

    @GetMapping("/api/client")
    public List<Client> getClients() {
        return dbServiceClient.findAll();
    }

}
