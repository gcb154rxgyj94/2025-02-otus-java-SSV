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
import java.util.List;
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
    public static TestClassResults run(String testClass) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (testClass == null || testClass.isEmpty()) {
            logger.error("Параметр testClass невалидный");
            throw new IllegalArgumentException("Параметр testClass null или пустой");
        }
        logger.info("Запускаем тестовый класс " + testClass);
        Class<?> testClazz = Class.forName(testClass);
        TestClassResults testClassResults = new TestClassResults();
        List<Method> beforeMethods = getMethodsWithAnnotations(testClazz, Before.class);
        List<Method> afterMethods = getMethodsWithAnnotations(testClazz, After.class);
        Arrays.stream(testClazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .forEach(method -> {
                    try {
                        testClassResults.add(testClass + "." + method.getName(), runTestMethods(testClazz, method, beforeMethods, afterMethods));
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return testClassResults;
    }

    /**
     * Запускаем тестовый метод
     *
     * @param testClazz - тестовый класса
     * @param testMethod - тестовый метод
     * @return - успешность прохождения теста
     */
    private static boolean runTestMethods(Class<?> testClazz, Method testMethod, List<Method> beforeMethods, List<Method> afterMethods) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Запускаем тестовый метод " + testClazz.getTypeName() + "." + testMethod.getName());
        Object testClassObject = testClazz.getDeclaredConstructor().newInstance();
        try {
            runMethodsOnObject(testClassObject, beforeMethods);
            testMethod.invoke(testClassObject);
            return true;
        } catch (Exception e) {
            logger.error(testClazz.getTypeName() + "." + testMethod.getName() + " упал с ошибкой " + e.getMessage());
            return false;
        } finally {
            try {
                runMethodsOnObject(testClassObject, afterMethods);
            } catch (Exception ignored) {
                logger.warn("Ошибка во время повторного выполнения After метода после теста");
                return false;
            }
        }
    }

    /**
     * Возвращает методы из класса с аннотацией
     *
     * @param testClass - класс
     * @param annotation - аннотация
     */
    private static List<Method> getMethodsWithAnnotations(Class<?> testClass, Class<? extends Annotation> annotation) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object testClassObject = testClass.getDeclaredConstructor().newInstance();
        return Arrays.stream(testClassObject.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .toList();
    }

    /**
     * Запускает переданные методы на классе
     *
     * @param testClassObject - экземпляр класса
     * @param methods - лист методов
     */
    private static void runMethodsOnObject(Object testClassObject, List<Method> methods) {
        methods.forEach(method -> {
            method.setAccessible(true);
            try {
                method.invoke(testClassObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
