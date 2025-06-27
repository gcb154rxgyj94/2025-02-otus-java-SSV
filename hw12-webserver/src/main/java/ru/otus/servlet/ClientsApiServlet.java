package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBService<Client> dbServiceClient;

    public ClientsApiServlet(DBService<Client> dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("clientName");
        String address = req.getParameter("clientAddress");
        String[] phoneNumbers = req.getParameterValues("clientPhone");
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
