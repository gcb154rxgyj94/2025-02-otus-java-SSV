package ru.otus.helpers;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DbServiceClient;
import ru.otus.crm.service.DbServiceUser;
import ru.otus.crm.service.ServiceFactory;
import ru.otus.crm.service.ServiceFactoryHibernate;

/**
 * Utils для работы базой данных
 */
public class DbServicesUtils {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static ServiceFactory serviceFactory;

    public static void initDBConnection() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class, Address.class, Phone.class, User.class
        );
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        serviceFactory = new ServiceFactoryHibernate(transactionManager);
        var dbServiceClient = (DbServiceClient) serviceFactory.getService(Client.class);
        var dbServiceUser = (DbServiceUser) serviceFactory.getService(User.class);
        FillDataBaseHelper.insertClients(dbServiceClient);
        FillDataBaseHelper.insertUsers(dbServiceUser);
    }

    public static DbServiceClient getDbServiceClient() {
        return (DbServiceClient) serviceFactory.getService(Client.class);
    }

    public static DbServiceUser getDbServiceUser() {
        return (DbServiceUser) serviceFactory.getService(User.class);
    }


}
