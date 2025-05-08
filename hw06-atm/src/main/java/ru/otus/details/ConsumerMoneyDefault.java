package ru.otus.details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Часть, отвечающая за принятие денег в АТМ
 */
@RequiredArgsConstructor
@Slf4j
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
     * Первичная валидация суммы (могут быть иные необходимые проверки)
     * @param banknote - банкнота
     * @return - прошла валидацию, или нет
     */
    private boolean validateBanknote(int banknote) {
        if (banknote <= 0) {
            log.error("Номинал банкноты некорректный (неположительный)");
            return false;
        }
        return true;
    }

}
