package edu.example.taskapp.service;

import edu.example.taskapp.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(String title) {
        tasks.add(new Task(title));
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public void replaceTasks(List<Task> savedTasks) {
        tasks.clear();
        tasks.addAll(savedTasks);
    }
}
