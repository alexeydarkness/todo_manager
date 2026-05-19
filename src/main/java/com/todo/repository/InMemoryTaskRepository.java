package com.todo.repository;

import com.todo.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository{

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private int nextId = 1;

    public int genId() {
        return nextId++;
    }
    @Override
    public void add(Task task) {
        task.setId(genId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void update(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void loadAll(List<Task> loaded) {
        tasks.clear();
        int maxId = 0;
        for(Task task : loaded) {
            tasks.put(task.getId(), task);
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        nextId = maxId + 1;
    }
}
