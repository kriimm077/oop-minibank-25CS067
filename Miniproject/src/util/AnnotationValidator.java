package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        return check(obj);
    }

    public static String[] validateWithFieldNames(Object obj) {
        return check(obj);
    }

    private static String[] check(Object obj) {
        Objects.requireNonNull(obj, "Object to validate must not be null.");
        List<String> errors = new ArrayList<>();

        Class<?> cls = obj.getClass();

        // Check each superclass too, since account fields are declared in Account.
        while (cls != null) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);

                if (!field.isAnnotationPresent(Positive.class)
                        && !field.isAnnotationPresent(MaxLength.class)) {
                    continue;
                }

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)
                            && value instanceof Number number
                            && !(number.doubleValue() > 0)) {
                        String message = field.getAnnotation(Positive.class).message();
                        errors.add(field.getName() + " " + message);
                    }

                    if (field.isAnnotationPresent(MaxLength.class)
                            && value instanceof String text) {
                        int max = field.getAnnotation(MaxLength.class).value();
                        if (text.length() > max) {
                            errors.add(field.getName() + " length must be at most " + max);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Could not read field " + field.getName() + " during validation.", e);
                }
            }

            cls = cls.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}