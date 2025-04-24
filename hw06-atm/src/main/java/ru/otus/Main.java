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
        ATM atm = ATM.builder()
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