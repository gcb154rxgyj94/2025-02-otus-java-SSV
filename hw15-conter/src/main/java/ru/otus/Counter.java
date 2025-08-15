package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    private static final int MAX_NUMBER = 10;
    private static final int MIN_NUMBER = 1;
    private final Object monitor = new Object();
    private int lastPrintThread = 0;
    private int lastAnswerThread = 0;

    private void print() {
        int plusNumber = 1;
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (monitor) {
                while (lastPrintThread != lastAnswerThread) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                lastPrintThread += plusNumber;
                logger.info(String.valueOf(lastPrintThread));
                sleep();
                if (lastPrintThread == MAX_NUMBER) {
                    plusNumber = -1;
                } else if (lastPrintThread == MIN_NUMBER) {
                    plusNumber = 1;
                }
            }
        }
    }

    private void answer() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (monitor) {
                if (lastPrintThread != lastAnswerThread) {
                    logger.info(String.valueOf(lastPrintThread));
                    sleep();
                    lastAnswerThread = lastPrintThread;
                }
                monitor.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread first = new Thread(counter::print);
        first.setName("first");
        Thread second = new Thread(counter::answer);
        second.setName("second");
        first.start();
        second.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(5_00);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
