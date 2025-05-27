package ru.otus.jdbc.mapper.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.crm.model.Id;
import ru.otus.jdbc.mapper.interfac.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    final Class<T> entityClass;

    String name;

    Constructor<T> constructor;

    Field idField;

    List<Field> fields;

    List<Field> fieldsWithoutId;

    @Override
    public String getName() {
        if (name == null) {
            name = entityClass.getSimpleName();
        }
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            try {
                constructor = entityClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            idField = Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> field.getAnnotation(Id.class) != null)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("У класса " + getName() + " нет поля с аннотацией @Id"));
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (fields == null) {
            fields = Arrays.asList(entityClass.getDeclaredFields());
        }
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = getAllFields().stream()
                    .filter(field -> !field.getName().equals(getIdField().getName()))
                    .collect(Collectors.toList());
        }
        return fieldsWithoutId;
    }

}
