package ru.otus.jdbc.mapper.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.jdbc.mapper.interfac.EntityClassMetaData;
import ru.otus.jdbc.mapper.interfac.EntitySQLMetaData;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    @Getter
    final EntityClassMetaData<?> entityClassMetaData;

    String selectAllSql;

    String selectByIdSql;

    String insertSql;

    String updateSql;

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            selectAllSql = new StringBuilder("select * from ").append(entityClassMetaData.getName().toLowerCase()).toString();
        }
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdSql == null) {
            selectByIdSql = new StringBuilder("select * from ")
                    .append(entityClassMetaData.getName().toLowerCase())
                    .append(" where id = ?").toString();
        }
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null) {
            StringBuilder stringBuilder = new StringBuilder("insert into ")
                    .append(entityClassMetaData.getName().toLowerCase())
                    .append("(");
            for (Field field: entityClassMetaData.getFieldsWithoutId()) {
                stringBuilder.append(field.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(") values(");
            for (int i = 0; i < entityClassMetaData.getFieldsWithoutId().size(); i++) {
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(")");
            insertSql = stringBuilder.toString();
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (updateSql == null) {
            StringBuilder stringBuilder = new StringBuilder("update ")
                    .append(entityClassMetaData.getName().toLowerCase())
                    .append(" set ");
            for (Field field: entityClassMetaData.getFieldsWithoutId()) {
                stringBuilder.append(field.getName())
                        .append(" = ?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            updateSql = stringBuilder.append(" where id = ?").toString();
        }
        return updateSql;
    }

}
