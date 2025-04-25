package ru.otus.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Тип банкноты номиналом в 500 монет
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FiveHundredTypeOfBanknote extends TypeOfBanknote {

    static FiveHundredTypeOfBanknote banknote = null;

    @Override
    public int getAmount() {
        return 500;
    }

    public static TypeOfBanknote getBanknote() {
        if (banknote == null) {
            banknote = new FiveHundredTypeOfBanknote();
        }
        return banknote;
    }

}
