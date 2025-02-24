package com.example.todo.service;

import com.example.todo.dto.TodoItemListsDTO;
import com.example.todo.model.TodoItem;
import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoItemRepository;
import com.example.todo.repository.TodoListNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private TodoListNameRepository todoListNameRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    private UUID listId;
    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
        listId = UUID.randomUUID();
        todoItem = new TodoItem();
        todoItem.setItemId(1L);
        todoItem.setTaskName("Test Task");
        todoItem.setDone(false);
        todoItem.setListId(listId);
    }

    @Test
    void testSaveTodoItem() {
        when(todoListNameRepository.findById(listId)).thenReturn(Optional.empty());
        when(todoListNameRepository.count()).thenReturn(0L);
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        TodoItem savedItem = todoItemService.saveTodoItem(todoItem);

        assertNotNull(savedItem);
        assertEquals("Test Task", savedItem.getTaskName());
        verify(todoItemRepository, times(1)).save(todoItem);
        verify(todoListNameRepository, times(1)).save(any(TodoListName.class));
    }

    @Test
    void testChangeDoneStateForTodoItem() {
        when(todoItemRepository.findByItemId(1L)).thenReturn(todoItem);
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        TodoItem updatedItem = todoItemService.changeDoneStateForTodoItem(1L);

        assertNotNull(updatedItem);
        assertTrue(updatedItem.getDone());
        verify(todoItemRepository, times(1)).save(todoItem);
    }

    @Test
    void testDeleteTodoItem() {
        when(todoItemRepository.findById(1L)).thenReturn(Optional.of(todoItem));
        doNothing().when(todoItemRepository).delete(todoItem);

        boolean isDeleted = todoItemService.deleteTodoItem(1L);

        assertTrue(isDeleted);
        verify(todoItemRepository, times(1)).delete(todoItem);
    }

    @Test
    void testEditTodoItem() {
        when(todoItemRepository.findById(1L)).thenReturn(Optional.of(todoItem));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        todoItem.setTaskName("Updated Task");
        TodoItem editedItem = todoItemService.editTodoItem(todoItem);

        assertNotNull(editedItem);
        assertEquals("Updated Task", editedItem.getTaskName());
        verify(todoItemRepository, times(1)).save(todoItem);
    }

    @Test
    void testGetTodoItemListIDs() {
        when(todoItemRepository.findDistinctListId()).thenReturn(List.of(listId));

        TodoItemListsDTO result = todoItemService.getTodoItemListIDs();

        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(todoItemRepository, times(1)).findDistinctListId();
    }
}
