package ru.otus;

import ru.otus.atm.ATM;

public class Main {
    public static void main(String[] args) {

        ATM atm = new ATM();
        atm.putMoney(5000);

        atm.getMoney(1000);

    }
}