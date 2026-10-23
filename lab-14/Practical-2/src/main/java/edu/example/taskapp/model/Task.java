package edu.example.taskapp.model;

import java.io.Serializable;

public record Task(String title) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Task {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank.");
        }
    }
}
