package com.example.todo.config;

import com.example.todo.model.TodoItem;
import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoItemRepository;
import com.example.todo.repository.TodoListNameRepository;
import com.example.todo.utils.HasLogger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DbPopulator implements HasLogger {
    private final Environment environment;
    private final TodoListNameRepository todoListNameRepository;
    private final TodoItemRepository todoItemRepository;
    private List<TodoListName> todoListNames;
    private List<TodoItem> todoItems;


    public DbPopulator(Environment environment, TodoListNameRepository todoListNameRepository, TodoItemRepository todoItemRepository) {
        this.environment = environment;
        this.todoListNameRepository = todoListNameRepository;
        this.todoItemRepository = todoItemRepository;
        if (Boolean.TRUE.equals(environment.getProperty("auto-populate-db", Boolean.class))) {
            initDB();
        }
    }

    private void initDB() {
        getLogger().info("Init DB");
        createTodoListNames();
        createTodoListForBusiness();
        createTodoListForHomework();
        createTodoListForPrivate();

    }

    private void createTodoListNames() {
        getLogger().info("Init DB - create 3 TodoListNames");
        todoListNames = new ArrayList<>(Arrays.asList(
                new TodoListName("To-Do List for business"),
                new TodoListName("To-Do List for homework"),
                new TodoListName("To-Do List for private"),
                new TodoListName("An empty To-Do List")
        ));

        todoListNameRepository.saveAll(todoListNames);
    }

    private void createTodoListForBusiness() {
        todoListNames.stream().filter(tln -> tln.getName().contains("business")).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Create a project plan", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Refactor the project java code", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Organize a code review meeting", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            todoItemRepository.saveAll(todoItems);
        });
    }

    private void createTodoListForHomework() {
        todoListNames.stream().filter(tln -> tln.getName().contains("homework")).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Write a summary of the last lesson", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Code the Java homework", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Review the java solution code", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            todoItemRepository.saveAll(todoItems);
        });
    }

    private void createTodoListForPrivate() {
        todoListNames.stream().filter(tln -> tln.getName().contains("private")).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Meet my friend in the city", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Go to grocery store", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Call Mam", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            todoItemRepository.saveAll(todoItems);
        });
    }
}
