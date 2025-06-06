package ru.otus.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestFailedBefore {

    private static final Logger logger = LoggerFactory.getLogger(TestFailedBefore.class);

    @Before
    public void before() {
        logger.info("before");
        throw new RuntimeException();
    }

    @Test
    public void test1() {
        logger.info("test1");
    }

    @Test
    public void test2() {
        logger.info("test2");
    }

    @Test
    public void test3() {
        logger.info("test3");
    }

    @After
    public void after() {
        logger.info("after");
    }

}
