package ru.otus.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class SimpleFailedTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleFailedTest.class);

    @Before
    public void before() {
        logger.info("Before method");
    }

    @Test
    public void test1() {
        logger.info("test1");
        throw new RuntimeException("Кидаем RuntimeException в ru.otus.tests.SimpleFailedTest.test1");
    }

    @Test
    public void test2() {
        logger.info("Test2");
    }

    @Test
    public void test3() {
        logger.info("Test3");
    }

    @After
    public void after() {
        logger.info("After method");
    }

}
