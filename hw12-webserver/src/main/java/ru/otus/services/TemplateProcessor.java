package ru.otus.services;

import java.io.IOException;
import java.util.Map;

/**
 * Интерфейс генерации web-страниц
 */
public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
}
