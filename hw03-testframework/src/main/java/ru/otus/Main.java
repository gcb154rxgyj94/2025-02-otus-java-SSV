package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.framework.TestClassResults;
import ru.otus.framework.TestFramework;

import java.lang.reflect.InvocationTargetException;
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

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.info("Начинаем запуск тестовых класов");
        for (String testClassName : TEST_CLASSES) {
            TestClassResults results = TestFramework.run(testClassName);
            results.printResults();
        }
    }
}