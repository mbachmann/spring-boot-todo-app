package com.example.todo.testcontainer.repository;

import com.example.todo.model.TodoItem;
import com.example.todo.repository.TodoItemRepository;
import com.example.todo.testcontainer.DBBaseTestContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class TodoItemRepositoryTest extends DBBaseTestContainer {

    @Autowired
    TodoItemRepository todoItemRepository;

    @BeforeEach
    public void setUp() {
        createTodoTestItems(todoItemRepository);
    }

    @AfterEach
    public void tearDown() {
        deleteTodoDoTestItems(todoItemRepository);
    }


    @Test
    public void getAllTodoListDetails() throws Exception {
        List<TodoItem> todoItems = todoItemRepository.findAll();
        assertFalse(todoItems.isEmpty());
    }

    @Test
    public void getAllTodoListsDetails() throws Exception {
        List<UUID> listIds = todoItemRepository.findDistinctListId();
        assertFalse(listIds.isEmpty());
    }


}
