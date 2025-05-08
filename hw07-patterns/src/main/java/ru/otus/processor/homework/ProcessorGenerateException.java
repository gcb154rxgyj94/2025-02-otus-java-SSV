package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.rmi.UnexpectedException;
import java.time.LocalTime;

public class ProcessorGenerateException implements Processor {

    @Override
    public Message process(Message message) {
        if (LocalTime.now().getSecond() % 2 == 0) {
            throw new RuntimeException("Exception on even second");
        }
        return message;
    }

}
