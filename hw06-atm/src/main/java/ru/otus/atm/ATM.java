package ru.otus.atm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.details.CellForMoney;
import ru.otus.details.CellsBlock;
import ru.otus.details.ConsumerMoneyInterface;
import ru.otus.details.GiverMoneyInterface;
import ru.otus.money.TypeOfBanknote;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс ATM (банкомант)
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ATM {

    final GiverMoneyInterface giverMoney;
    final ConsumerMoneyInterface consumerMoney;
    int totalSum;

    public static ATMBuilder builder() {
        return new ATMBuilder();
    }

    /**
     * Положить банкноту в банкомат
     * @param banknote - номинал банкноты
     */
    public ATM putMoney(int banknote) {
        if (consumerMoney.putMoney(banknote)) {
            log.info("Положили в банкомат " +  banknote + " монет");
            totalSum += banknote;
            return this;
        }
        log.error("Не удалось положить в банкомат " +  banknote + " монет");
        return this;
    }

    /**
     * Выдать сумму из банкомата
     * @param sum - требуемая сумма
     */
    public ATM getMoney(int sum) {
        if (verifySum(sum)) {
            log.error("В банкомате недостаточно средств для выдачи " + sum);
            return this;
        }
        if (giverMoney.getMoney(sum)) {
            totalSum -= sum;
            return this;
        }
        log.error("Операция выдачи денег не удалась");
        return this;
    }

    /**
     * Выдать оставшуюся сумму из банкомата
     */
    public int getRemainingSum() {
        return giverMoney.getRemainingSum();
    }

    /**
     * Проверка требуемой суммы
     */
    private boolean verifySum(int sum) {
        return totalSum < sum;
    }

    /**
     * Билдер ATM
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ATMBuilder {

        final Set<TypeOfBanknote> typeOfBanknotes = new HashSet<>();
        Class<? extends ConsumerMoneyInterface> consumerMoneyClass;
        Class<? extends GiverMoneyInterface> giverMoneyClass;

        /**
         * Добавление поддержки типа банкноты
         */
        public ATMBuilder addBanknote(TypeOfBanknote typeOfBanknote) {
            log.info("Добавляем в ATM поодержку банкноты номиналом: {}", typeOfBanknote.getAmount());
            typeOfBanknotes.add(typeOfBanknote);
            return this;
        }

        /**
         * Добавление детали для принятия денег
         */
        public ATMBuilder consumerMoney(Class<? extends ConsumerMoneyInterface> consumerMoneyClass) {
            log.info("Добавляем в ATM деталь для принятия денег: {}", consumerMoneyClass);
            this.consumerMoneyClass = consumerMoneyClass;
            return this;
        }

        /**
         * Добавление детали для выдачи денег
         */
        public ATMBuilder giverMoney(Class<? extends GiverMoneyInterface> giverMoneyClass) {
            log.info("Добавляем в ATM деталь для снятия денег: {}", giverMoneyClass);
            this.giverMoneyClass = giverMoneyClass;
            return this;
        }

        /**
         * Создание ATM
         */
        public ATM build() throws Exception {

            if (typeOfBanknotes.isEmpty()) {
                throw new IllegalArgumentException("Список поддерживаемых банкнот пустой");
            } else if (consumerMoneyClass == null) {
                throw new IllegalArgumentException("Нет класса для принятия денег");
            } else if (giverMoneyClass == null) {
                throw new IllegalArgumentException("Нет класса для выдачи денег");
            }

            log.info("Создаем ATM");
            CellsBlock.CellsBlockBuilder cellsBlockBuilder = CellsBlock.builder();
            typeOfBanknotes.forEach(typeOfBanknote -> cellsBlockBuilder.addCell(new CellForMoney(typeOfBanknote)));
            CellsBlock cellsBlock = cellsBlockBuilder.build();
            return new ATM(
                    giverMoneyClass.getDeclaredConstructor(CellsBlock.class).newInstance(cellsBlock),
                    consumerMoneyClass.getDeclaredConstructor(CellsBlock.class).newInstance(cellsBlock)
            );

        }

    }

}
