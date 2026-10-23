package edu.example.taskapp.app;

import edu.example.taskapp.model.Task;
import edu.example.taskapp.service.TaskFileStore;
import edu.example.taskapp.service.TaskService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final Path DATA_FILE = Path.of("tasks.dat");

    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        TaskFileStore fileStore = new TaskFileStore();

        try {
            if (java.nio.file.Files.exists(DATA_FILE)) {
                taskService.replaceTasks(fileStore.load(DATA_FILE));
                System.out.println("Loaded " + taskService.getTaskCount() + " saved task(s).");
            }
        } catch (IOException exception) {
            System.out.println("Could not load saved tasks: " + exception.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nTask menu: add, list, save, exit");
                System.out.print("Choose an action: ");
                String action = scanner.nextLine().trim().toLowerCase();

                switch (action) {
                    case "add" -> {
                        System.out.print("Task title: ");
                        taskService.addTask(scanner.nextLine());
                        System.out.println("Task added.");
                    }
                    case "list" -> {
                        if (taskService.getTasks().isEmpty()) {
                            System.out.println("There are no tasks yet.");
                        } else {
                            for (Task task : taskService.getTasks()) {
                                System.out.println("- " + task.title());
                            }
                        }
                    }
                    case "save" -> saveTasks(fileStore, taskService);
                    case "exit" -> running = false;
                    default -> System.out.println("Choose add, list, save, or exit.");
                }
            }
        }
    }

    private static void saveTasks(TaskFileStore fileStore, TaskService taskService) {
        try {
            fileStore.save(DATA_FILE, taskService.getTasks());
            System.out.println("Saved " + taskService.getTaskCount() + " task(s).");
        } catch (IOException exception) {
            System.out.println("Could not save tasks: " + exception.getMessage());
        }
    }
}
