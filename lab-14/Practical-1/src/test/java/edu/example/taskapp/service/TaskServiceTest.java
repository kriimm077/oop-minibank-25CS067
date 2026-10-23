package edu.example.taskapp.service;

import edu.example.taskapp.app.TaskMenu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskServiceTest {
    @Test
    void addingTaskIncreasesCount() {
        TaskService service = new TaskService();

        service.addTask("Finish lab");

        assertEquals(1, service.getTaskCount());
    }

    @Test
    void addingSecondTaskIncreasesCountAgain() {
        TaskService service = new TaskService();
        service.addTask("Read chapter");

        service.addTask("Practice Java");

        assertEquals(2, service.getTaskCount());
    }

    @Test
    void unknownMenuActionThrowsException() {
        TaskMenu menu = new TaskMenu(new TaskService());

        assertThrows(IllegalArgumentException.class, () -> menu.handleAction("delete"));
    }
}
