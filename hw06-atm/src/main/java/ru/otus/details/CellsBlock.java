package ru.otus.details;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Блок со всеми ячейками
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CellsBlock {

    final TreeSet<CellForMoney> cells;

    public static CellsBlockBuilder builder() {
        return new CellsBlockBuilder();
    }

    /**
     * Положить банкноту в ячейку
     * @param banknote - банкнота
     * @return - успешность операции
     */
    public boolean put(int banknote) {
        for (CellForMoney cell: cells) {
            if (cell.isSuitableForCell(banknote)) {
                cell.incrementCount();
                System.out.println("Банкнота " + banknote + " была успешно положена в банкомат");
                return true;
            }
        }
        System.out.println("Банкомант не принимает банкноту номиналом " + banknote);
        return false;
    }

    /**
     * Выдать сумму из ячеек
     * @param sum - сумма
     * @return - успешность операции
     */
    public boolean get(int sum) {
        if (validateSum(sum)) {
            Map<CellForMoney, Integer> needBanknotesFromEveryCell = canGetByExistBanknotes(sum);
            if (needBanknotesFromEveryCell.isEmpty()) {
                return false;
            }
            for (Map.Entry<CellForMoney, Integer> cellForMoneyIntegerEntry: needBanknotesFromEveryCell.entrySet()) {
                for (int i = 0; i < cellForMoneyIntegerEntry.getValue(); i++) {
                    cellForMoneyIntegerEntry.getKey().decrementCount();
                }
            }
            System.out.println("Сумма " + sum + " была успешно выдана");
            return true;
        }
        return false;
    }

    /**
     * Вернуть минимальный номинал банкнот
     * @return int - минимальный номинал банкнот
     */
    private CellForMoney getCellWithMinimalBanknote() {
        return cells.first();
    }

    /**
     * Валидация суммы
     * @param sum - сумма
     * @return - успешность проверки
     */
    private boolean validateSum(int sum) {
        if (getCellWithMinimalBanknote().canGetSumOfBanknotes(sum)) {
            System.out.println("Запрашиваему сумму невозможно выдать существующими банкнотами");
            return false;
        }
        return true;
    }

    /**
     * Может ли сумма быть выдана существующими банкнотами
     * @param sum - сумма
     * @return - список требуемых банкнот (если сумма не может быть выдана - список пустой)
     */
    private Map<CellForMoney, Integer> canGetByExistBanknotes(int sum) {
        Map<CellForMoney, Integer> needBanknotesFromEveryCell = new HashMap<>();
        int remainingSum = sum;
        for (CellForMoney cellForMoney: cells.descendingSet()) {
            for (int i = 1; cellForMoney.canGetCountOfBanknotes(i) && remainingSum >= sum; i++) {
                needBanknotesFromEveryCell.put(cellForMoney, needBanknotesFromEveryCell.computeIfAbsent(cellForMoney, key -> 0) + 1);
                remainingSum -= cellForMoney.getSumByCountBanknotes(1);
            }
        }
        if (remainingSum != 0) {
            System.out.println("В банкоманте нет достаточного количества банкнот для выдачи суммы " + sum);
            return Map.of();
        }
        return needBanknotesFromEveryCell;
    }

    /**
     * Билдер создания CellsBlock
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CellsBlockBuilder {

        final TreeSet<CellForMoney> cellForMonies = new TreeSet<>();

        /**
         * Добавление ячейки в блок
         * @param cellForMoney - тип банкноты
         * @return - билдер
         */
        public CellsBlock.CellsBlockBuilder addCell(CellForMoney cellForMoney) {
            cellForMonies.add(cellForMoney);
            return this;
        }

        /**
         * Создание CellsBlock
         * @return - CellsBlock
         */
        public CellsBlock build() throws Exception {
            if (cellForMonies.isEmpty()) {
                throw new IllegalArgumentException("В блоке нет ячеек с деньгами");
            }
            return new CellsBlock(cellForMonies);
        }

    }

}
