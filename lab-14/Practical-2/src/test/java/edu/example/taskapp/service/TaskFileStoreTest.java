package edu.example.taskapp.service;

import edu.example.taskapp.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskFileStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void savesAndReloadsTasksUnchanged() throws Exception {
        TaskFileStore fileStore = new TaskFileStore();
        Path file = temporaryDirectory.resolve("saved-tasks.dat");
        List<Task> originalTasks = List.of(
                new Task("Prepare presentation"),
                new Task("Review notes")
        );

        fileStore.save(file, originalTasks);
        List<Task> loadedTasks = fileStore.load(file);

        assertEquals(originalTasks, loadedTasks);
    }
}
