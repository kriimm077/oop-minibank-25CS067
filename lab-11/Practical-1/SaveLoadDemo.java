import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveLoadDemo {

    private static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int studentId;
        private transient String temporaryNote;

        public Student(String name, int studentId, String temporaryNote) {
            this.name = name;
            this.studentId = studentId;
            this.temporaryNote = temporaryNote;
        }

        @Override
        public String toString() {
            return "Student{name='" + name + "', studentId=" + studentId
                    + ", temporaryNote='" + temporaryNote + "'}";
        }
    }

    public static void main(String[] args) {
        Path file = Paths.get("students.ser");
        Student[] students = {
                new Student("Asha", 101, "Temporary note for Asha"),
                new Student("Ravi", 102, "Temporary note for Ravi")
        };

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(file.toFile()))) {
            output.writeObject(students);
            System.out.println("Saved students to " + file.toAbsolutePath());
        } catch (IOException exception) {
            System.out.println("Could not save students: " + exception.getMessage());
            return;
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(file.toFile()))) {
            Student[] loadedStudents = (Student[]) input.readObject();

            System.out.println("Loaded students:");
            for (Student student : loadedStudents) {
                System.out.println(student);
            }
            System.out.println("The names and IDs survived. The transient notes are null "
                    + "because transient fields are not serialized.");
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Could not load students: " + exception.getMessage());
        }
    }
}
