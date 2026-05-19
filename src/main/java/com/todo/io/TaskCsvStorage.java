package com.todo.io;

import com.todo.model.Priority;
import com.todo.model.Status;
import com.todo.model.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TaskCsvStorage {

    private static final String DELIMITER = ";";


    public void save(List<Task> tasks, Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for(Task task : tasks) {
                writer.write(toCsv(task));
                writer.newLine();
            }
        }
    }

    public List<Task> load(Path file) throws IOException {
        if(!Files.exists(file)) {
            throw new IOException("Файл не найден " + file);
        }
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(fromCsv(line));
            }
        }
        return tasks;
    }

    private String toCsv(Task task) {
        return String.join(DELIMITER,
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getPriority().name(),
                task.getStatus().name(),
                task.getDueDate().toString()
        );
    }

    private Task fromCsv(String line) {
        String[] parts = line.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        String title = parts[1];
        String description = parts[2];
        Priority priority = Priority.valueOf(parts[3]);
        Status status = Status.valueOf(parts[4]);
        LocalDate dueDate = LocalDate.parse(parts[5]);
        return new Task(id, title, description, priority, status, dueDate);
    }

}
