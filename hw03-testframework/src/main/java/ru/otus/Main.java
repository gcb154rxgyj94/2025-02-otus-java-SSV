package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.framework.TestFramework;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Set<String> TEST_CLASSES = Set.of(
            "ru.otus.tests.SimpleTest",
            "ru.otus.tests.SimpleFailedTest",
            "ru.otus.tests.TestFailedAfter",
            "ru.otus.tests.TestFailedBefore"
    );

    public static void main(String[] args) throws ClassNotFoundException {
        logger.info("Начинаем запуск тестовых класов");
        for (String testClassName : TEST_CLASSES) {
            Map<String, Boolean> results = TestFramework.run(testClassName);
            Set<String> successTest = new HashSet<>();
            Set<String> failedTest = new HashSet<>();
            results.forEach((k, v) -> {
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
}