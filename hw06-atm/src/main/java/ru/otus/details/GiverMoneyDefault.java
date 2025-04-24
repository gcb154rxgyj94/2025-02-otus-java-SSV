package ru.otus.details;

import lombok.RequiredArgsConstructor;

/**
 * Часть, отвечающая за выдачу денег из АТМ
 */
@RequiredArgsConstructor
public class GiverMoneyDefault implements GiverMoneyInterface {

    final CellsBlock cellsBlock;

    /**
     * Выдать сумму
     * @param sum - требуемая сумма денег
     */
    @Override
    public boolean getMoney(int sum){
        if (validateSum(sum)) {
            return cellsBlock.get(sum);
        }
        return false;
    }

    /**
     * Первичная валидация суммы (могут быть иные необходимые проверки)
     * @param sum - сумма
     * @return - прошла валидацию, или нет
     */
    private boolean validateSum(int sum) {
        if (sum <= 0) {
            System.out.println("Некорректная сумма для выдачи");
            return false;
        }
        return true;
    }

}
