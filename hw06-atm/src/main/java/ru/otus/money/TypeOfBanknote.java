package ru.otus.money;

/**
 * Тип банкноты определенного номинала
 */
public abstract class TypeOfBanknote {

    /**
     * Вернуть номинал банкноты
     * @return - номинал банкноты
     */
    abstract public int getAmount();

}
