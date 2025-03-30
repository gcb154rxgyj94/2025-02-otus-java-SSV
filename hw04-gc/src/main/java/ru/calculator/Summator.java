package ru.calculator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private final List<Data> listValues = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();

    public void calc(Data data) {
        int dataValue = data.value();
        if ((listValues.size() + 1) % 100_000 == 0) {
            listValues.clear();
        } else {
            listValues.add(data);
        }
        sum += dataValue + random.nextInt();

        sumLastThreeValues = dataValue + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = dataValue;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (dataValue + 1) - sum);
            someValue = someValue & 0x7fffffff + listValues.size();
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
