package com.example.todo.repository;

import com.example.todo.AbstractTest;
import com.example.todo.TodoApplication;
import com.example.todo.model.TodoItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes= TodoApplication.class)
public class TodoItemRepositoryTest extends AbstractTest {

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
        assertTrue(todoItems.size() > 0);
    }

    @Test
    public void getAllTodoListsDetails() throws Exception {
        List<UUID> listIds = todoItemRepository.findDistinctListId();
        assertTrue(listIds.size() > 0);
    }


}
