package ru.otus.details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Часть, отвечающая за выдачу денег из АТМ
 */
@RequiredArgsConstructor
@Slf4j
public class GiverMoneyDefault implements GiverMoneyInterface {

    final CellsBlock cellsBlock;

    /**
     * Выдать сумму sum
     */
    @Override
    public boolean getMoney(int sum){
        if (validateSum(sum)) {
            return cellsBlock.get(sum);
        }
        return false;
    }

    /**
     * Выдать оставшуюся сумму
     */
    @Override
    public int getRemainingSum() {
        return cellsBlock.getRemainingSum();
    }

    /**
     * Первичная валидация суммы (могут быть иные необходимые проверки)
     * @param sum - сумма
     * @return - прошла валидацию, или нет
     */
    private boolean validateSum(int sum) {
        if (sum <= 0) {
            log.error("Некорректная сумма для выдачи (неположительная)");
            return false;
        }
        return true;
    }

}
