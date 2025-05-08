package ru.otus.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Тип банкноты номиналом в 100 монет
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OneHundredTypeOfBanknote extends TypeOfBanknote {

    static OneHundredTypeOfBanknote banknote = null;

    @Override
    public int getAmount() {
        return 100;
    }

    public static TypeOfBanknote getBanknote() {
        if (banknote == null) {
            banknote = new OneHundredTypeOfBanknote();
        }
        return banknote;
    }

}
