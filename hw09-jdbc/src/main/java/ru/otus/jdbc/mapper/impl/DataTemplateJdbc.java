package ru.otus.jdbc.mapper.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.interfac.EntityClassMetaData;
import ru.otus.jdbc.mapper.interfac.EntitySQLMetaData;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    ArrayList<T> list = new ArrayList<>();
                    try {
                        while (rs.next()) {
                            list.add(createObject(rs));
                        }
                        return list;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private T createObject(ResultSet rs) throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        T entity = entityClassMetaData.getConstructor().newInstance();
        for (Field field: entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            field.set(entity, rs.getObject(field.getName()));
        }
        Field fieldId = entityClassMetaData.getIdField();
        fieldId.setAccessible(true);
        fieldId.set(entity, rs.getObject("id"));
        return entity;
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            List<Object> fields = getFieldsValueWithoutId(entity);
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    fields
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            List<Object> fields = getFieldsValueWithoutId(entity);
            fields.add(entityClassMetaData.getIdField().get(entity));
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    fields);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldsValueWithoutId(T entity) {
        return entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

}
