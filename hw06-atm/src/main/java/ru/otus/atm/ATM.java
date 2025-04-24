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

import java.util.ArrayList;
import java.util.List;

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
            totalSum += banknote;
        }
        return this;
    }

    /**
     * Выдать сумму из банкомата
     * @param sum - требуемая сумма
     */
    public ATM getMoney(int sum) {
        if (verifySum(sum)) {
            System.out.println("В банкомате недостаточно средст для выдачи " + sum);
            return this;
        }
        if (giverMoney.getMoney(sum)) {
            totalSum -= sum;
        }
        return this;
    }

    /**
     * Проверка требуемой суммы
     * @param sum - требуемая сумма
     * @return - хвататет ли средств для выдачи суммы
     */
    private boolean verifySum(int sum) {
        return totalSum >= sum;
    }

    /**
     * Билдер создания ATM
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ATMBuilder {

        final List<TypeOfBanknote> typeOfBanknotes = new ArrayList<>();
        Class<? extends ConsumerMoneyInterface> consumerMoneyClass;
        Class<? extends GiverMoneyInterface> giverMoneyClass;

        /**
         * Добавление поддержки банкноты
         * @param typeOfBanknote - тип банкноты
         * @return - билдер
         */
        public ATMBuilder addBanknote(TypeOfBanknote typeOfBanknote) {
            log.info("Добавляем в ATM поодержку банкноты номиналом: {}", typeOfBanknote.getAmount());
            typeOfBanknotes.add(typeOfBanknote);
            return this;
        }

        /**
         * Добавление детали для принятия денег
         * @param consumerMoneyClass - класс
         * @return - билдер
         */
        public ATMBuilder consumerMoney(Class<? extends ConsumerMoneyInterface> consumerMoneyClass) {
            this.consumerMoneyClass = consumerMoneyClass;
            return this;
        }

        /**
         * Добавление детали для выдачи денег
         * @param giverMoneyClass - класс
         * @return - билдер
         */
        public ATMBuilder giverMoney(Class<? extends GiverMoneyInterface> giverMoneyClass) {
            this.giverMoneyClass = giverMoneyClass;
            return this;
        }

        /**
         * Создание ATM
         * @return - ATM
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
