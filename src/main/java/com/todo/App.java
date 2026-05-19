package com.todo;

import com.todo.repository.InMemoryTaskRepository;
import com.todo.repository.TaskRepository;
import com.todo.service.TaskService;
import com.todo.ui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(taskRepository);
        ConsoleUI consoleUI = new ConsoleUI(taskService);
        consoleUI.start();
    }
}
