package edu.example.taskapp.model;

public record Task(String title) {
    public Task {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank.");
        }
    }
}
