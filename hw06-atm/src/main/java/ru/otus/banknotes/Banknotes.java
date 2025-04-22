package ru.otus.banknotes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Перечисление возможных номиналов банкнот
 */
@Getter
@AllArgsConstructor
public enum Banknotes {

    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    final int denomination;

    public static int getMinimalDenomination() {
        return Arrays.stream(Banknotes.values()).mapToInt(b -> b.denomination).min().getAsInt();
    }

}
