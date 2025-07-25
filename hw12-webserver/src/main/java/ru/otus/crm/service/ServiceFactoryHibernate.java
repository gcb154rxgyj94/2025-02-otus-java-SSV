package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.User;

/**
 * Фабрика сервисов БД через Hibernate
 */
public class ServiceFactoryHibernate implements ServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(ServiceFactoryHibernate.class);
    private final TransactionManagerHibernate transactionManagerHibernate;

    public ServiceFactoryHibernate(TransactionManagerHibernate transactionManagerHibernate) {
        this.transactionManagerHibernate = transactionManagerHibernate;
    }

    @Override
    public <T> DBService<T> getService(Class<T> entityClass) {
        if (entityClass.equals(User.class)) {
            var userTemplate = new DataTemplateHibernate<>(User.class);
            return (DBService<T>) new DbServiceUser(transactionManagerHibernate, userTemplate, createCache());
        } else if (entityClass.equals(Client.class)) {
            var clientTemplate = new DataTemplateHibernate<>(Client.class);
            return (DBService<T>) new DbServiceClient(transactionManagerHibernate, clientTemplate, createCache());
        }
        throw new IllegalArgumentException("No service found for class: " + entityClass.getName());
    }

    private static <T> MyCache<String, T> createCache() {
        MyCache<String, T> dbServiceCache = new MyCache<>();
        dbServiceCache.addListener(new HwListener<>() {
            @Override
            public void notify(String key, T value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
        return dbServiceCache;

    }

}
