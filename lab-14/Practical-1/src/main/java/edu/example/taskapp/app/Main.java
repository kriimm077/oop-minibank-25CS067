package edu.example.taskapp.app;

import edu.example.taskapp.service.TaskService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        TaskMenu menu = new TaskMenu(taskService);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nTask menu: add, list, exit");
                System.out.print("Choose an action: ");
                String action = scanner.nextLine();

                try {
                    System.out.println(menu.handleAction(action));
                    switch (action.trim().toLowerCase()) {
                        case "add" -> {
                            System.out.print("Task title: ");
                            taskService.addTask(scanner.nextLine());
                            System.out.println("Task added.");
                        }
                        case "exit" -> running = false;
                        default -> {
                            // Listing is handled by TaskMenu.
                        }
                    }
                } catch (IllegalArgumentException exception) {
                    System.out.println("Invalid input: " + exception.getMessage());
                }
            }
        }
    }
}
