package ru.otus.dataprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Measurement;

@Slf4j
public class ResourcesFileLoader implements Loader {

    private static final ObjectMapper mapper = JsonMapper.builder().build();
    private static final ClassLoader classLoader = ResourcesFileLoader.class.getClassLoader();
    private final String resourcesFile;

    public ResourcesFileLoader(String fileName) {
        resourcesFile = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcesFile)) {
            if (inputStream == null) {
                log.error("Файл не найден: " + resourcesFile);
                return Collections.emptyList();
            }
            return mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            log.error("Ошибка при работе с потоками из файла " + resourcesFile + ": " + e.getMessage(), e);
            throw new FileProcessException(e);
        }
    }
}
