# Todo Manager
Консольное приложение для управления списком задач. Учебный проект по Java Core.
## Возможности
- Создание, удаление задач
- Изменение статуса (TODO / IN_PROGRESS / DONE)
- Приоритеты (LOW / MEDIUM / HIGH)
- Фильтрация по статусу и приоритету
- Поиск задач по названию
- Сортировка по дате и приоритету
- Просмотр просроченных задач
- Статистика по статусам
- Сохранение и загрузка в CSV
## Стек
- Java 17+
- Maven
- Collections (HashMap, ArrayList)
- Stream API
- File I/O (NIO.2)
## Архитектура

```
src/main/java/com/todo/
├── App.java              — точка входа
├── model/                — доменная модель (Task, Priority, Status)
├── repository/           — слой хранения (интерфейс + реализация)
├── service/              — бизнес-логика
├── ui/                   — консольный интерфейс
├── io/                   — сохранение/загрузка CSV
└── exception/            — кастомные исключения
```
## Запуск
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.todo.App"
```
Или просто запустить `App.java` из IDE.
## Формат CSV
Каждая строка — одна задача, поля разделены `;`:
id;title;description;priority;status;dueDate
1;Купить хлеб;белый и чёрный;HIGH;TODO;2025-06-01
