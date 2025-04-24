package ru.otus.details;
import lombok.RequiredArgsConstructor;
import ru.otus.money.TypeOfBanknote;

/**
 * Ячейка для банкнот одного номинала
 */
@RequiredArgsConstructor
public class CellForMoney implements Comparable<CellForMoney> {

    final TypeOfBanknote typeOfBanknote;
    int countBanknote = 0;

    /**
     * Положить банкноту в ячейку
     */
    public void incrementCount() {
        countBanknote += 1;
    }

    /**
     * Выдать банкноту из ячейки
     */
    public boolean decrementCount() {
        if(countBanknote > 0) {
            countBanknote -= 1;
            return true;
        }
        return false;
    }

    /**
     * Првоерка на совпадение бакноты номиналу ячейки
     * @param banknote1 - номинал банкноты
     * @return - совпадение
     */
    public boolean isSuitableForCell(int banknote1) {
        return typeOfBanknote.getAmount() == banknote1;
    }

    /**
     * Првоерка на возможность выдать сумму только данным номиналом
     * @param sum - сумма
     * @return - возможность
     */
    public boolean canGetSumOfBanknotes(int sum) {
        return sum % typeOfBanknote.getAmount() == 0;
    }

    /**
     * Проверка на возможность получения определенного количества банкнот
     * @param count - количество банкнот
     * @return - возможость получения count банкнот
     */
    public boolean canGetCountOfBanknotes(int count) {
        return countBanknote >= count;
    }

    /**
     * Возвращает сумму при заданном количестве банкнот
     * @param count - количество банкнот
     * @return - сумма
     */
    public int getSumByCountBanknotes(int count) {
        return count * typeOfBanknote.getAmount();
    }


    @Override
    public int compareTo(CellForMoney cellForMoney) {
        return Integer.compare(this.typeOfBanknote.getAmount(), cellForMoney.typeOfBanknote.getAmount());
    }
}
