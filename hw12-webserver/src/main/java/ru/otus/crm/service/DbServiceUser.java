package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с User
 */
public class DbServiceUser implements DBService<User> {
    private static final Logger log = LoggerFactory.getLogger(DbServiceUser.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, User> userCache;

    public DbServiceUser(TransactionManager transactionManager,
                         DataTemplate<User> userDataTemplate,
                         HwCache<String, User> userCache) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
        this.userCache = userCache;
    }

    @Override
    public User save(User user) {
        User user1 = transactionManager.doInTransaction(session -> {
            var userCloned = user.clone();
            if (user.getId() == null) {
                var savedUser = userDataTemplate.insert(session, userCloned);
                log.info("created user: {}", userCloned);
                return savedUser;
            }
            var savedUser = userDataTemplate.update(session, userCloned);
            log.info("updated user: {}", savedUser);
            return savedUser;
        });
        userCache.put(getKey(user1.getId()), user1.clone());
        return user1;
    }

    @Override
    public Optional<User> get(long id) {
        Optional<User> user = Optional.of(userCache.get(getKey(id)));
        if (user.isPresent()) {
            return user;
        }
        user = transactionManager.doInReadOnlyTransaction(session -> {
            var userOptional = userDataTemplate.findById(session, id);
            log.info("user: {}", userOptional);
            return userOptional;
        });
        user.ifPresent(value -> userCache.put(getKey(value.getId()), value.clone()));
        return user;
    }

    @Override
    public Optional<User> getFirstByField(String field, String value) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var usersList = userDataTemplate.findByEntityField(session, field, value);
            Optional<User> optionalUser = usersList.isEmpty() ? Optional.empty() : Optional.of(usersList.get(0));
            log.info("user: {}", optionalUser);
            return optionalUser;
        });
    }

    @Override
    public List<User> findAll() {
        List<User> list = transactionManager.doInReadOnlyTransaction(session -> {
            var userList = userDataTemplate.findAll(session);
            log.info("userList:{}", userList);
            return userList;
        });
        list.forEach(value -> userCache.put(getKey(value.getId()), value.clone()));
        return list;
    }

    private String getKey(long id) {
        return String.valueOf(id);
    }

}
