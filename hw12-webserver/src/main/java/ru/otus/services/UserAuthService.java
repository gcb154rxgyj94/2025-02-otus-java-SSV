package ru.otus.services;

/**
 * Сервис аутентификации пользователей
 */
public interface UserAuthService {
    boolean authenticate(String login, String password);
}
