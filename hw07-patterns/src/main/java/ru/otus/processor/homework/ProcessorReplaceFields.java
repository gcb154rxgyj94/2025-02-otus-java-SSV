package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorReplaceFields implements Processor {

    @Override
    public Message process(Message message) {
        String f11 = message.getField11();
        String f12 = message.getField12();
        return message.toBuilder().field11(f12).field12(f11).build();
    }

}
