package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorGenerateException;
import ru.otus.processor.homework.ProcessorReplaceFields;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HomeWork {

    public static void main(String[] args) {
        List<Processor> processors = List.of(new ProcessorReplaceFields(), new LoggerProcessor(new ProcessorGenerateException(LocalTime::now)));

        ComplexProcessor complexProcessor = new ComplexProcessor(processors, ex -> {
            log.info(ex.getMessage());
        });
        HistoryListener historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        ObjectForMessage field13 = new ObjectForMessage();
        ArrayList<String> field13Data = new ArrayList<>();
        field13Data.add("field13_1");
        field13Data.add("field13_2");
        field13.setData(field13Data);
        Message message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();
        log.info("result:{}", complexProcessor.handle(message));
        Message message1 = historyListener.findMessageById(message.getId()).get();
        log.info("result:{}", message1);
        log.info("result:{}", complexProcessor.handle(message1));
        complexProcessor.removeListener(historyListener);
    }
}
