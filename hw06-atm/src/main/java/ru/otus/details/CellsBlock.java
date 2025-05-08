package ru.otus.details;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Блок со всеми ячейками
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CellsBlock {

    final TreeSet<CellForMoney> cells;

    /**
     * Билдер создания CellsBlock
     */
    public static CellsBlockBuilder builder() {
        return new CellsBlockBuilder();
    }

    /**
     * Положить банкноту в ячейку
     */
    public boolean put(int banknote) {
        for (CellForMoney cell: cells) {
            if (cell.isSuitableForCell(banknote)) {
                cell.incrementCount();
                log.info("Банкнота " + banknote + " была успешно положена в ячейку");
                return true;
            }
        }
        log.error("Банкомант не принимает банкноту номиналом " + banknote);
        return false;
    }

    /**
     * Выдать сумму из ячеек
     */
    public boolean get(int sum) {
        if (validateSum(sum)) {
            Map<CellForMoney, Integer> needBanknotesFromEveryCell = canGetByExistBanknotes(sum);
            if (needBanknotesFromEveryCell.isEmpty()) {
                log.error("В банкоманте нет достаточного количества банкнот для выдачи суммы " + sum);
                return false;
            }
            for (var cellForMoneyIntegerEntry: needBanknotesFromEveryCell.entrySet()) {
                for (int i = 0; i < cellForMoneyIntegerEntry.getValue(); i++) {
                    cellForMoneyIntegerEntry.getKey().decrementCount();
                }
            }
            log.info("Сумма " + sum + " была успешно выдана");
            return true;
        }
        return false;
    }

    /**
     * Выдать оставшуюся сумму из ячеек
     */
    public int getRemainingSum() {
        int sum = 0;
        for (CellForMoney cell: cells) {
            while (cell.decrementCount()) {
                sum += cell.getSumByCountBanknotes(1);
            }
        }
        log.info("Сумма " + sum + " была успешно выдана");
        return sum;
    }

    /**
     * Валидация суммы
     */
    private boolean validateSum(int sum) {
        if (!getCellWithMinimalBanknote().canGetSumOfBanknotes(sum)) {
            log.error("Запрашиваему сумму невозможно выдать существующими банкнотами, сумма не кратна минимальному номиналу");
            return false;
        }
        return true;
    }

    /**
     * Вернуть минимальный номинал банкнот
     * @return int - минимальный номинал банкнот
     */
    private CellForMoney getCellWithMinimalBanknote() {
        return cells.first();
    }

    /**
     * Может ли сумма быть выдана существующими в блоке банкнотами
     * @return - список требуемых банкнот (если сумма не может быть выдана - список пустой)
     */
    private Map<CellForMoney, Integer> canGetByExistBanknotes(int sum) {
        Map<CellForMoney, Integer> needBanknotesFromEveryCell = new HashMap<>();
        int remainingSum = sum;
        for (CellForMoney cellForMoney: cells.descendingSet()) {
            for (int i = 1; cellForMoney.canGetCountOfBanknotes(i) && remainingSum >= cellForMoney.getSumByCountBanknotes(1); i++) {
                needBanknotesFromEveryCell.put(cellForMoney, needBanknotesFromEveryCell.computeIfAbsent(cellForMoney, key -> 0) + 1);
                remainingSum -= cellForMoney.getSumByCountBanknotes(1);
            }
        }
        return remainingSum != 0 ? Map.of() : needBanknotesFromEveryCell;
    }

    /**
     * Билдер создания CellsBlock
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CellsBlockBuilder {

        final TreeSet<CellForMoney> cellForMonies = new TreeSet<>();

        /**
         * Добавление ячейки в блок
         */
        public CellsBlock.CellsBlockBuilder addCell(CellForMoney cellForMoney) {
            cellForMonies.add(cellForMoney);
            return this;
        }

        /**
         * Создание CellsBlock
         */
        public CellsBlock build() {
            if (cellForMonies.isEmpty()) {
                throw new IllegalArgumentException("В блоке нет ячеек с деньгами");
            }
            return new CellsBlock(cellForMonies);
        }

    }

}
