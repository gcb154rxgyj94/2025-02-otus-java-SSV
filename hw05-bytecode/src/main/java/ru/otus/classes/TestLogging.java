package ru.otus.classes;

import ru.otus.Log;

public class TestLogging implements TestLoggingInterface {

    TestLogging() {}

    @Log
    public void calculation(int param1) {
        System.out.println("calculation with 1 param");
    }

    public void calculation(int param1, int param2) {
        System.out.println("calculation with 2 params");
    }

    @Log
    public void calculation(int param1, int param2, int param3) {
        System.out.println("calculation with 3 params");
    }


}
