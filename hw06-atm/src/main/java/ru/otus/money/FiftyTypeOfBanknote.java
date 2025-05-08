package ru.otus.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Тип банкнот номиналом в 50 монет
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FiftyTypeOfBanknote extends TypeOfBanknote {

    static FiftyTypeOfBanknote banknote = null;

    @Override
    public int getAmount() {
        return 50;
    }

    public static TypeOfBanknote getBanknote() {
        if (banknote == null) {
            banknote = new FiftyTypeOfBanknote();
        }
        return banknote;
    }

}
