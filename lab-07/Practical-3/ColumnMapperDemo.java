import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "marks")
    int marks;

    @Column(name = "city")
    String city;

    public String toString() {
        return "Student{id=" + id + ", name=" + name + ", marks=" + marks + ", city=" + city + "}";
    }
}

class Mapper {
    static <T> T fill(Class<T> cls, String[] header, String[] row) throws Exception {
        T obj = cls.getDeclaredConstructor().newInstance();

        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            String columnName = field.getAnnotation(Column.class).name();
            int position = -1;

            for (int i = 0; i < header.length; i++) {
                if (header[i].equals(columnName)) {
                    position = i;
                    break;
                }
            }

            if (position == -1 || position >= row.length) {
                System.out.println("Warning: column '" + columnName + "' not found, skipping field " + field.getName());
                continue;
            }

            String value = row[position];
            field.setAccessible(true);

            if (field.getType() == int.class) {
                field.setInt(obj, Integer.parseInt(value));
            } else {
                field.set(obj, value);
            }
        }

        return obj;
    }
}

public class ColumnMapperDemo {
    public static void main(String[] args) throws Exception {
        String[] header = {"id", "name", "marks"};
        String[] row = {"101", "Aarav", "88"};

        Student s = Mapper.fill(Student.class, header, row);
        System.out.println(s);
    }
}