package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DBService;

/**
 * Аутентицикация пользователей через DBServiceUser
 */
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final DBService<User> dbServiceUser;

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceUser.getFirstByField("login", login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
