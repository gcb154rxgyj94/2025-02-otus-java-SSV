package ru.otus.banknotes;

import lombok.Getter;

/**
 * Ячейка для банкнот одного номинала
 */
@Getter
public class CellForMoney {

    int countBanknote = 0;

    /**
     * Положить банкноты в ячейку
     */
    public void incrementCount() {
        countBanknote += 1;
    }

    /**
     * Выдать банкноту из ячейки
     */
    public void decrementCount() {
        countBanknote -= 1;
    }


}
