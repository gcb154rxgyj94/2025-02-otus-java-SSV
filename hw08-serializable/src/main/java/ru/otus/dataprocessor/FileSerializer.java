package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class FileSerializer implements Serializer {

    private static final ObjectMapper mapper = JsonMapper.builder().build();
    private final String resourcesFile;

    public FileSerializer(String fileName) {
        resourcesFile = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var file = new File(resourcesFile);
        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            log.error("Не получилось записать в файл " + resourcesFile + ": " + e.getMessage(), e);
            throw new FileProcessException(e);
        }
    }
}
