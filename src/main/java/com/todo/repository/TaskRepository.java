package com.todo.repository;

import com.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void add(Task task);
    void remove(int id);
    void update(Task task);
    Optional<Task> findById(int id);
    List<Task> findAll();
    void loadAll(List<Task> tasks);
}
