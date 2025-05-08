package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.atm.ATM;
import ru.otus.details.ConsumerMoneyDefault;
import ru.otus.details.GiverMoneyDefault;
import ru.otus.money.FiftyTypeOfBanknote;
import ru.otus.money.FiveHundredTypeOfBanknote;
import ru.otus.money.FiveThousandTypeOfBanknote;
import ru.otus.money.OneHundredTypeOfBanknote;
import ru.otus.money.OneThousandTypeOfBanknote;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        ATM atm = createATM();
        // далее любые действия с банкоматом

        log.info("Кладем существующие типа банкнот");
        atm.putMoney(50)
                .putMoney(100)
                .putMoney(500)
                .putMoney(1000)
                .putMoney(5000);
        log.info("В банкомате 6650");

        log.info("Кладем неуществующие типа банкнот");
        atm.putMoney(400).putMoney(10);

        log.info("Запрашиваем выдачу сум");
        atm.getMoney(100)
                .getMoney(550)
                .getMoney(6000);
        log.info("В банкомате " + atm.getRemainingSum());

        log.info("Пытаемся еще взять сумму");
        atm.getMoney(100);

        log.info("Пытаемся взять сумму, невозможную выдать существующими в банкомате банкнотами");
        atm.putMoney(5000)
                .getMoney(100);

        log.info("Пытаемся взять сумму несколькими банкнотами");
        atm.putMoney(1000)
                .putMoney(1000)
                .putMoney(500)
                .getMoney(2000)
                .putMoney(50)
                .putMoney(100)
                .getMoney(5150);
        log.info("Оставшаяся сумма " + atm.getRemainingSum());
    }

    private static ATM createATM() throws Exception {
        return ATM.builder()
                .consumerMoney(ConsumerMoneyDefault.class)
                .giverMoney(GiverMoneyDefault.class)
                .addBanknote(FiftyTypeOfBanknote.getBanknote())
                .addBanknote(OneHundredTypeOfBanknote.getBanknote())
                .addBanknote(FiveHundredTypeOfBanknote.getBanknote())
                .addBanknote(OneThousandTypeOfBanknote.getBanknote())
                .addBanknote(FiveThousandTypeOfBanknote.getBanknote())
                .build();
    }

}