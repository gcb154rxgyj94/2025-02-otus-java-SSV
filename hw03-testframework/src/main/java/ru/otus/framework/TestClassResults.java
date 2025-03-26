package ru.otus.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Результаты прохождения тестового класса
 */
public class TestClassResults {

    private static final Logger logger = LoggerFactory.getLogger(TestClassResults.class);
    private final Map<String, Boolean> testMethodsResults = new HashMap<>();

    /**
     * Добавить результат прохождения тестового метода
     * @param methodsName - название метода
     * @param result - результат прохождения
     */
    public void add(String methodsName, boolean result) {
        testMethodsResults.put(methodsName, result);
    }

    /**
     * Вывести актуальные результаты прохождения тестового класса
     */
    public void printResults() {
        Set<String> successTest = new HashSet<>();
        Set<String> failedTest = new HashSet<>();
        testMethodsResults.forEach((k, v) -> {
            if (v) {
                successTest.add(k);
            } else {
                failedTest.add(k);
            }
        });
        logger.info("Всего тестовых методов {}\nУспешно прошли: {}\n{}\nНе прошли: {}\n{}",
                successTest.size() + failedTest.size(),
                successTest.size(),
                String.join("\n", successTest),
                failedTest.size(),
                String.join("\n", failedTest));
    }

}
