package ru.otus.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Тип банкноты номиналом в 5000 монет
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FiveThousandTypeOfBanknote extends TypeOfBanknote {

    static FiveThousandTypeOfBanknote banknote = null;

    @Override
    public int getAmount() {
        return 5000;
    }

    public static TypeOfBanknote getBanknote() {
        if (banknote == null) {
            banknote = new FiveThousandTypeOfBanknote();
        }
        return banknote;
    }

}
