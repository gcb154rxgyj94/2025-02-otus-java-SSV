package ru.otus.details;

import lombok.RequiredArgsConstructor;

/**
 * Часть, отвечающая за принятие денег в АТМ
 */
@RequiredArgsConstructor
public class ConsumerMoneyDefault implements ConsumerMoneyInterface {

    final CellsBlock cellsBlock;

    /**
     * Положить банкноту в ячейку
     * @param banknote - номинал банкноты
     */
    @Override
    public boolean putMoney(int banknote) {
        if (validateBanknote(banknote)) {
            return cellsBlock.put(banknote);
        }
        return false;
    }

    /**
     * Валидация банкноты
     * @param banknote - банкнота
     * @return - прошла валидацию или нет
     */
    private boolean validateBanknote(int banknote) {
        if (banknote <= 0) {
            System.out.println("Номинал банкноты некорректный");
            return false;
        }
        return true;
    }

}
