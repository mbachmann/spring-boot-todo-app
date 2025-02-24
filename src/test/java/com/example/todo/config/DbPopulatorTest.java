package com.example.todo.config;

import com.example.todo.repository.TodoItemRepository;
import com.example.todo.repository.TodoListNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbPopulatorTest {

    @Mock
    private Environment environment;

    @Mock
    private TodoListNameRepository todoListNameRepository;

    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private DbPopulator dbPopulator;

    @BeforeEach
    void setUp() {
        when(environment.getProperty("auto-populate-db", Boolean.class)).thenReturn(true);
        dbPopulator = new DbPopulator(environment, todoListNameRepository, todoItemRepository);
    }

    @Test
    void shouldPopulateDatabaseWhenAutoPopulateIsTrue() {
        // Verify that the saveAll method is called for todo list names
        verify(todoListNameRepository, times(1)).saveAll(any(List.class));

        // Verify that the saveAll method is called for todo items
        verify(todoItemRepository, atLeastOnce()).saveAll(any(List.class));
    }

}
