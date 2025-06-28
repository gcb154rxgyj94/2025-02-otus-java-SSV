package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * API-Сервлет для Client
 */
@RequiredArgsConstructor
public class ClientsApiServlet extends HttpServlet {

    private static final String CLIENT_NAME_PARAM = "clientName";
    private static final String CLIENT_ADDRESS_PARAM = "clientAddress";
    private static final String CLIENT_PHONE_PARAM = "clientPhone";
    private final transient DBService<Client> dbServiceClient;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter(CLIENT_NAME_PARAM);
        String address = req.getParameter(CLIENT_ADDRESS_PARAM);
        String[] phoneNumbers = req.getParameterValues(CLIENT_PHONE_PARAM);
        Client client = new Client(name);
        if (address != null && !address.isEmpty()) {
            client.setAddress(new Address(address));
        }

       if (phoneNumbers != null && phoneNumbers.length > 0) {
           List<Phone> phones = new ArrayList<>();
           for (String number : phoneNumbers) {
               if (number != null && !number.isEmpty()) {
                   phones.add(new Phone(number));
               }
           }
           client.setPhones(phones);
       }
        dbServiceClient.save(client);
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

}
