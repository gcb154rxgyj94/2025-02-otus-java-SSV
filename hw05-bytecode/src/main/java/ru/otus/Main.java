package ru.otus;

import ru.otus.classes.Ioc;
import ru.otus.classes.TestLoggingInterface;


public class Main {

    public static void main(String[] args) {
        TestLoggingInterface testClass = Ioc.createMyClass();
        testClass.calculation(1);
        testClass.calculation(1,2);
        testClass.calculation(1,2,3);
    }

}
