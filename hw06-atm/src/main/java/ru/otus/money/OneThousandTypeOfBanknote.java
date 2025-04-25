package ru.otus.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Тип банкноты номиналом в 1000 монет
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OneThousandTypeOfBanknote extends TypeOfBanknote {

    static OneThousandTypeOfBanknote banknote = null;

    @Override
    public int getAmount() {
        return 1000;
    }

    public static TypeOfBanknote getBanknote() {
        if (banknote == null) {
            banknote = new OneThousandTypeOfBanknote();
        }
        return banknote;
    }

}
