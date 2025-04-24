package ru.otus.money;

/**
 * Банкнота определенного номинала
 */
public abstract class TypeOfBanknote {

    /**
     * Вернуть номинал банкноты
     * @return - номинал банкноты
     */
    abstract public int getAmount();

}
