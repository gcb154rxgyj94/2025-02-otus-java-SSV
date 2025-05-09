package ru.otus.processor.homework;

import lombok.AllArgsConstructor;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.rmi.UnexpectedException;
import java.time.LocalTime;

@AllArgsConstructor
public class ProcessorGenerateException implements Processor {

    final TimeProvider timeProvider;

    @Override
    public Message process(Message message) {
        if (timeProvider.getTime().getSecond() % 2 == 0) {
            throw new RuntimeException("Exception on even second");
        }
        return message;
    }

}
