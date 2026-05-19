package com.todo.ui;

import com.todo.exception.TaskNotFoundException;
import com.todo.io.TaskCsvStorage;
import com.todo.model.Priority;
import com.todo.model.Status;
import com.todo.model.Task;
import com.todo.service.TaskService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final TaskService service;
    private final Scanner scanner;
    private final TaskCsvStorage storage = new TaskCsvStorage();


    private static final String MENU = """
                    0. Показать меню
                    1. Создать задачу
                    2. Удалить задачу
                    3. Отметить как DONE
                    4. Отметить как IN_PROGRESS
                    5. Показать все задачи
                    6. Фильтр по статусу
                    7. Фильтр по приоритету
                    8. Просроченные
                    9. Поиск по названию
                    10. Сортировка по дате
                    11.Сортировка по приоритету
                    12. Статистика
                    13. Сохранить файл
                    14. Загрузить из файла
                    15. Выход
                    """;
    public ConsoleUI(TaskService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }
    public void start() {
        System.out.println(MENU);
        while (true) {
            System.out.println("Выберите пункт меню: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 0 -> System.out.println(MENU);
                    case 1 -> {
                        String title = readString("Введите заголовок");
                        String description = readString("Введите описание");
                        Priority priority = readPriority("Введите приоритет для задачи");
                        LocalDate dueDate = readDate("Введите дату для задачи");
                        service.createTask(title, description, priority, dueDate);
                    }
                    case 2 -> {
                        service.deleteTask(readId("Введите id для удаления"));
                    }
                    case 3 -> {
                        service.markAsDone(readId("Введите id для пометки"));
                    }
                    case 4 -> {
                        service.markInProgress(readId("Введите id для пометки"));
                    }
                    case 5 -> printAllTasks(service.getAllTasks());
                    case 6 -> {
                        Status status = readStatus("Введите статус для фильтра");
                        printAllTasks(service.getByStatus(status));
                    }
                    case 7 -> {
                        Priority priority = readPriority("Введите приоритет для фильтра");
                        printAllTasks(service.getByPriority(priority));
                    }
                    case 8 -> printAllTasks(service.getOverdueTasks())  ;
                    case 9 ->  {
                        String title = readString("Введите заголовок для поиска");
                        printAllTasks(service.searchByTitle(title));
                    }
                    case 10 -> {
                        printAllTasks(service.sortByDueDate());
                    }
                    case 11 -> printAllTasks(service.sortByPriority());
                    case 12 -> printStatistics(service.getStatistics());
                    case 13 -> {
                        String path = readString("Введите путь к файлу для сохранения");
                        try {
                            storage.save(service.getAllTasks(), Path.of(path));
                            System.out.println("Сохранено");
                        } catch (IOException e) {
                            System.out.println("Ошибка сохранения: " + e.getMessage());
                        }
                    }
                    case 14 -> {
                        String path = readString("Введите путь к файлу для загрузки");
                        try {
                            List<Task> loaded = storage.load(Path.of(path));
                            service.loadTasks(loaded);
                            System.out.println("Загружено задач: " + loaded.size());
                        } catch (IOException e) {
                            System.out.println("Ошибка загрузки: " + e.getMessage());
                        }
                    }
                    case 15 -> {
                        return;
                    }
                    default -> System.out.println("Некорректный ввод");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            } catch (TaskNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printAllTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("Задача " + (i + 1) + " - " + tasks.get(i));
        }
    }

    public void printStatistics(Map<Status, Long> map) {
        map.forEach((status, count) -> System.out.println(status + ": " + count));
    }
    private int readId(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }
    }
    private Priority readPriority(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return Priority.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный формат для приоритета");
            }
        }
    }

    private Status readStatus(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return Status.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный формат для статуса");
            }
        }
    }
    private LocalDate readDate(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты, используйте YYYY-MM-DD");
            }
        }
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

}
