package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor();

    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
