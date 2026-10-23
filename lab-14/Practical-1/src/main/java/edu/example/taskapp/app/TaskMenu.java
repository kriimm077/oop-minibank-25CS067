package edu.example.taskapp.app;

import edu.example.taskapp.service.TaskService;

public class TaskMenu {
    private final TaskService taskService;

    public TaskMenu(TaskService taskService) {
        this.taskService = taskService;
    }

    public String handleAction(String action) {
        return switch (action.trim().toLowerCase()) {
            case "add" -> "Enter a task title.";
            case "list" -> taskService.getTasks().isEmpty()
                    ? "There are no tasks yet."
                    : taskService.getTasks().toString();
            case "exit" -> "Goodbye!";
            default -> throw new IllegalArgumentException(
                    "Unknown action. Choose add, list, or exit.");
        };
    }
}
