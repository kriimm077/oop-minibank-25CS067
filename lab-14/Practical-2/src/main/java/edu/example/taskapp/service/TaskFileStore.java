package edu.example.taskapp.service;

import edu.example.taskapp.model.Task;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TaskFileStore {
    public void save(Path file, List<Task> tasks) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(file))) {
            output.writeObject(tasks);
        }
    }

    public List<Task> load(Path file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object savedData;
            try {
                savedData = input.readObject();
            } catch (ClassNotFoundException exception) {
                throw new IOException("Saved task class could not be found.", exception);
            }

            if (!(savedData instanceof List<?> savedList)) {
                throw new IOException("File does not contain a task list.");
            }

            for (Object item : savedList) {
                if (!(item instanceof Task)) {
                    throw new IOException("File contains an invalid task.");
                }
            }

            @SuppressWarnings("unchecked")
            List<Task> tasks = (List<Task>) savedList;
            return List.copyOf(tasks);
        }
    }
}
