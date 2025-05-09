package ru.otus.dataprocessor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> result = new TreeMap<>(Comparator.naturalOrder());
        data.forEach(measurement -> result.put(measurement.name(), result.computeIfAbsent(measurement.name(), key -> 0.0) + measurement.value()));
        return result;
    }
}
