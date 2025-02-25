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

    private static final String PRIVATE_LIST = "To-Do List for private";
    private static final String BUSINESS_LIST = "To-Do List for business";
    private static final String HOMEWORK_LIST = "To-Do List for homework";
    private static final String EMTPY_LIST = "An empty To-Do List";


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
        getLogger().info("Init DB - create 4 TodoListNames");
        todoListNames = new ArrayList<>(Arrays.asList(
                new TodoListName(BUSINESS_LIST),
                new TodoListName(HOMEWORK_LIST),
                new TodoListName(PRIVATE_LIST),
                new TodoListName(EMTPY_LIST)
        ));
        todoListNames.forEach(
                tln -> todoListNameRepository.findByName(tln.getName()).stream().findFirst().ifPresentOrElse(
                        found -> getLogger().info("Init DB - TodoListName already exists: {}", found.getName()),
                        () -> todoListNameRepository.save(tln)));
        todoListNames = todoListNameRepository.findAll();
    }

    private void createTodoListForBusiness() {
        todoListNames.stream().filter(tln -> tln.getName().equals(BUSINESS_LIST)).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Create a project plan", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Refactor the project java code", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Organize a code review meeting", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            saveTodoItems(todoItems);
        });
    }



    private void createTodoListForHomework() {
        todoListNames.stream().filter(tln -> tln.getName().equals(HOMEWORK_LIST)).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Write a summary of the last lesson", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Code the Java homework", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Review the java solution code", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            saveTodoItems(todoItems);
        });
    }

    private void createTodoListForPrivate() {
        todoListNames.stream().filter(tln -> tln.getName().equals(PRIVATE_LIST)).findFirst().ifPresent(tln -> {
            todoItems = new ArrayList<>(Arrays.asList(
                    new TodoItem(tln.getId(), "Meet my friend in the city", Instant.now().minusSeconds(3600 * 24 * 10)),
                    new TodoItem(tln.getId(), "Go to grocery store", Instant.now().minusSeconds(3600 * 24 * 5)),
                    new TodoItem(tln.getId(), "Call Mam", Instant.now().minusSeconds(3600 * 24 * 5))
            ));
            saveTodoItems(todoItems);
        });
    }

    private void saveTodoItems(List<TodoItem> todoItems) {
        todoItems.forEach(todoItem -> {
            todoItemRepository.findByTaskNameAndListId(todoItem.getTaskName(), todoItem.getListId()).stream().findFirst().ifPresentOrElse(
                    found -> getLogger().info("Init DB - TodoItem already exists: {}", found.getTaskName()),
                    () -> todoItemRepository.save(todoItem)
            );
        });
    }
}
