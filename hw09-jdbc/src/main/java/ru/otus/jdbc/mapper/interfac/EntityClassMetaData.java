package ru.otus.jdbc.mapper.interfac;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Интерфейс для разбивки класса на составные части
 * @param <T> - класс
 */
public interface EntityClassMetaData<T> {

    String getName();

    Constructor<T> getConstructor();

    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
