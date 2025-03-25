package ru.otus.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Запуск тестов
 */
public class TestFramework {

    private static final Logger logger = LoggerFactory.getLogger(TestFramework.class);

    /**
     * Запускаем тестовый фреймворк
     *
     * @param testClass - тестовый класс
     * @return - результаты выполнения тестовых методов
     */
    public static Map<String, Boolean> run(String testClass) throws ClassNotFoundException {
        if (testClass == null || testClass.isEmpty()) {
            logger.error("Параметр testClass невалидный");
            throw new IllegalArgumentException("testClass is null or empty");
        }
        logger.info("Запускаем тестовый класс " + testClass);
        Class<?> testClazz = Class.forName(testClass);
        Map<String, Boolean> resultMethods = new HashMap<>();
        Arrays.stream(testClazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .forEach(method -> {
                    try {
                        resultMethods.put(testClass + "." + method.getName(), runTestMethods(testClazz, method));
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return resultMethods;
    }

    /**
     * Запускаем тестовый метод
     *
     * @param testClazz - тестовый класса
     * @param testMethod - тестовый метод
     * @return - успешность прохождения теста
     */
    private static boolean runTestMethods(Class<?> testClazz, Method testMethod) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Запускаем тестовый метод " + testClazz.getTypeName() + "." + testMethod.getName());
        Object testClassObject = testClazz.getDeclaredConstructor().newInstance();
        try {
            runMethodsWithAnnotations(testClassObject, Before.class);
            testMethod.invoke(testClassObject);
            runMethodsWithAnnotations(testClassObject, After.class);
            return true;
        } catch (Exception e) {
            logger.error(testClazz.getTypeName() + "." + testMethod.getName() + " упал с ошибкой " + e.getMessage());
            return false;
        }
    }

    /**
     * Запускаем методы из класа с аннотацией
     *
     * @param testClassObject - экземпляр класса
     * @param annotation - аннотация
     */
    private static void runMethodsWithAnnotations(Object testClassObject, Class<? extends Annotation> annotation) {
        Arrays.stream(testClassObject.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .forEach(method -> {
                    method.setAccessible(true);
                    try {
                        method.invoke(testClassObject);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
