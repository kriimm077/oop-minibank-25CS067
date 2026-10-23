package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import model.annotation.Id;

public class Repository<T> {
    private final HashMap<String, T> items = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null.");
        }

        String id = findId(entity);
        items.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final ArrayList<T> saveAll(T... entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entity array must not be null.");
        }

        ArrayList<T> savedEntities = new ArrayList<>(entities.length);
        for (T entity : entities) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    private String findId(T entity) {
        Field idField = null;
        Class<?> currentClass = entity.getClass();

        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (idField != null) {
                        throw new IllegalStateException(
                                "Entity has more than one field marked with @Id.");
                    }
                    idField = field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        if (idField == null) {
            throw new IllegalArgumentException("Entity type "
                    + entity.getClass().getName() + " has no field marked with @Id.");
        }

        try {
            idField.setAccessible(true);
            Object value = idField.get(entity);
            if (value == null || value.toString().isBlank()) {
                throw new IllegalArgumentException("@Id field must contain a non-empty value.");
            }
            return value.toString();
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(
                    "Could not read the @Id field " + idField.getName() + ".", exception);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be null or empty.");
        }

        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null.");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }
}
