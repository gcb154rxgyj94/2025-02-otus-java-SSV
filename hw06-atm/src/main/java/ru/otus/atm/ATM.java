package ru.otus.atm;

import ru.otus.banknotes.Banknotes;
import ru.otus.banknotes.CellForMoney;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Класс ATM (банкомант)
 */
public class ATM {

    final EnumMap<Banknotes, CellForMoney> CELLS = new EnumMap<>(Banknotes.class);
    int totalSum = 0;

    public ATM() {
        for (Banknotes banknotes: Banknotes.values()) {
            CELLS.put(banknotes, new CellForMoney());
        }
    }

    /**
     * Положить банкноту в банкомат
     * @param banknote - номинал банкноты
     */
    public void putMoney(int banknote) {
        for (Banknotes banknotes: Banknotes.values()) {
            if (banknotes.getDenomination() == banknote) {
                CELLS.get(banknotes).incrementCount();
                totalSum += banknote;
                System.out.println("Банкнота " + banknote + " была успешно положена в банкомат");
                return;
            }
        }
        System.out.println("Банкомант не принимает банкноту номиналом " + banknote);
    }

    /**
     * Выдать сумму
     * @param sum - сумма
     */
    public void getMoney(int sum){
        EnumMap<Banknotes, Integer> needBanknotes = new EnumMap<>(Banknotes.class);

        for (Banknotes banknotes : Banknotes.values()) {
            needBanknotes.put(banknotes, 0);
        }
        if (sum > totalSum) {
            System.out.println("В банкомате недостаточно средст для выдачи " + sum);
            return;
        }
        if (sum % Banknotes.getMinimalDenomination() != 0) {
            System.out.println("Запрашиваему сумму невозможно выдать существующими банкнотами");
            return;
        }

        int tempSum = sum;
        List<Map.Entry<Banknotes, CellForMoney>> entryList = new ArrayList<>(CELLS.entrySet());
        entryList.sort((entry1, entry2) -> Integer.compare(entry2.getKey().getDenomination(), entry1.getKey().getDenomination()));
        for (Map.Entry<Banknotes, CellForMoney> entry: entryList) {
            CellForMoney cellForMoney = entry.getValue();
            for (int i = 0; i < cellForMoney.getCountBanknote(); i++) {
                while (entry.getKey().getDenomination() <= tempSum) {
                    needBanknotes.put(entry.getKey(), needBanknotes.get(entry.getKey()) + 1);
                    tempSum -= entry.getKey().getDenomination();
                }
            }
        }
        if (tempSum != 0) {
            System.out.println("В банкоманте нет достаточного количества банкнот для выдачи суммы " + sum);
            return;
        }

        for (Map.Entry<Banknotes, Integer> banknotes1: needBanknotes.entrySet()) {
            totalSum -= banknotes1.getKey().getDenomination() * banknotes1.getValue();
            CellForMoney tempCellForMoney = CELLS.get(banknotes1.getKey());
            for (int i = 0; i < banknotes1.getValue(); i++) {
                tempCellForMoney.decrementCount();
            }
        }
        System.out.println("Сумма " + sum + " была успешно выдана");
    }

}
