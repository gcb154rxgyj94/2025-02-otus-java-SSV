package ru.otus.helpers;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DbServiceClient;
import ru.otus.crm.service.DbServiceUser;

import java.util.List;

/**
 * Хелпер для заполнения базы данных
 */
public final class FillDataBaseHelper {

    /**
     * Заполняем таблицу клиентов
     * @param dbServiceClient - клиент для работы с клиентами
     */
    public static void insertClients(DbServiceClient dbServiceClient) {
        if (dbServiceClient.findAll().isEmpty()) {
            dbServiceClient.save(new Client("firstClient"));
            dbServiceClient.save(new Client("secondClient", new Address("Cenrtal")));
            dbServiceClient.save(new Client("thirdClient", List.of(new Phone("87645678899"))));
            dbServiceClient.save(new Client("fourthClient", List.of(new Phone("87645672899"), new Phone("81234567890"))));
            dbServiceClient.save(new Client("fifthClient", new Address("Street"), List.of(new Phone("87645678898"), new Phone("81434567890"))));
        }
    }

    /**
     * Заполняем таблицу юзеров
     * @param dbServiceUser - клиент для работы с юзерами
     */
    public static void insertUsers(DbServiceUser dbServiceUser) {
        if (dbServiceUser.findAll().isEmpty()) {
            dbServiceUser.save(new User("firstClient", "login1", "pswd1"));
            dbServiceUser.save(new User("firstClient", "login2", "pswd2"));
            dbServiceUser.save(new User("firstClient", "login3", "pswd3"));
        }
    }


}
