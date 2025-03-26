package ru.otus.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class SimpleTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Before
    public void before1() {
        logger.info("before1");
    }

    @Before
    public void before2() {
        logger.info("before2");
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
    public void after1() {
        logger.info("after1");
    }

    @After
    public void after2() {
        logger.info("after2");
    }

}
