package ru.otus.details;

/**
 * Интерфейс для части выдачи денег из ATM
 */
public interface GiverMoneyInterface {

    boolean getMoney(int sum);

}
