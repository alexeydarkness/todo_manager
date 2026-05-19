package com.todo.service;

import com.todo.exception.TaskNotFoundException;
import com.todo.model.*;
import com.todo.repository.TaskRepository;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void createTask(String title, String description, Priority priority, LocalDate dueDate) {
        Task task = new Task(0, title, description, priority, Status.TODO, dueDate);
        repository.add(task);
    }

    public void deleteTask(int id) {
        if (repository.findById(id).isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        repository.remove(id);
    }

    public void markAsDone(int id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(Status.DONE);
        repository.update(task);
    }

    public void markInProgress(int id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(Status.IN_PROGRESS);
        repository.update(task);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public List<Task> getByStatus(Status status) {
        return repository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getByPriority(Priority priority) {
        return repository.findAll().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        return repository.findAll().stream()
                .filter(task -> task.getDueDate().isBefore(LocalDate.now()))
                .filter(task -> task.getStatus() != Status.DONE)
                .collect(Collectors.toList());
    }

    public List<Task> searchByTitle(String keyword) {
        return repository.findAll().stream()
                .filter(task -> task.getTitle().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Task> sortByDueDate() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Task> sortByPriority() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Task::getPriority))
                .collect(Collectors.toList());
    }

    public Map<Status, Long> getStatistics() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Task::getStatus,
                        Collectors.counting()
                ));
    }

    public void loadTasks(List<Task> tasks) {
        repository.loadAll(tasks);
    }

}
