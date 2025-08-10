package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);
    private final PriorityBlockingQueue<SensorData> buffer;
    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.buffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public synchronized void process(SensorData data) {
        buffer.add(data);
        log.info("Добавили данные в buffer: {}", data);
        if (buffer.size() >= bufferSize){
            flush();
        }
    }

    public synchronized void flush() {
        try {
            int size = buffer.size();
            if (size != 0) {
                List<SensorData> list = new ArrayList<>(buffer.size());
                for(int i = 0; i < size; i++) {
                    list.add(buffer.poll());
                }
                buffer.clear();
                writer.writeBufferedData(list);
                log.info("Отправили буфферизованные данные: {} штук", size);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

}

