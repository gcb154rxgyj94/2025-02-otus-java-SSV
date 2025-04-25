package ru.otus.details;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.money.TypeOfBanknote;

/**
 * Ячейка для банкнот одного типа
 */
@RequiredArgsConstructor
@Slf4j
public class CellForMoney implements Comparable<CellForMoney> {

    final TypeOfBanknote typeOfBanknote;
    int countBanknote = 0;

    /**
     * Положить банкноту в ячейку
     */
    public void incrementCount() {
        log.info("В ячейку положили банкноту номиналом " + typeOfBanknote.getAmount());
        countBanknote += 1;
    }

    /**
     * Выдать банкноту из ячейки
     */
    public boolean decrementCount() {
        if(countBanknote > 0) {
            log.info("Из ячейки выдали банкноту номиналом " + typeOfBanknote.getAmount());
            countBanknote -= 1;
            return true;
        }
        log.error("В ячейке с банкнотами номинала " + typeOfBanknote.getAmount() + " больше нет купюр");
        return false;
    }

    /**
     * Проверка на совпадение бакноты номиналу ячейки
     */
    public boolean isSuitableForCell(int banknote) {
        return typeOfBanknote.getAmount() == banknote;
    }

    /**
     * Првоерка на возможность выдать сумму только данным типом банкнот
     */
    public boolean canGetSumOfBanknotes(int sum) {
        return sum % typeOfBanknote.getAmount() == 0;
    }

    /**
     * Проверка на возможность получения определенного количества банкнот
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
