package com.example.todo.rest;

import com.example.todo.TodoApplication;
import com.example.todo.dto.TodoItemListsDTO;
import com.example.todo.dto.TodoItemsDTO;
import com.example.todo.model.TodoItem;
import com.example.todo.rest.advice.ResourceNotFoundException;
import com.example.todo.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes=TodoApplication.class)
@ExtendWith(MockitoExtension.class)
class TodoItemControllerTest {

    @Mock
    private TodoItemService todoItemService;

    @InjectMocks
    private TodoItemController todoItemController;

    private TodoItem sampleItem;

    @BeforeEach
    void setUp() {
        sampleItem = new TodoItem();
        sampleItem.setItemId(1L);
        sampleItem.setTaskName("Sample Task");
    }

    @Test
    void testGetItem_Success() {
        when(todoItemService.getItem(1L)).thenReturn(sampleItem);
        TodoItem result = todoItemController.getItem(1L);
        assertEquals(sampleItem, result);
    }

    @Test
    void testGetItem_NotFound() {
        when(todoItemService.getItem(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> todoItemController.getItem(1L));
    }

    @Test
    void testGetItemsOfOneList() {
        UUID listId = UUID.randomUUID();
        when(todoItemService.getAllTodoItemsForListId(listId)).thenReturn(Collections.singletonList(sampleItem));
        List<TodoItem> result = todoItemController.getItemsOfOneList(listId);
        assertEquals(1, result.size());
        assertEquals(sampleItem, result.get(0));
    }

    @Test
    void testGetListIDs() {
        TodoItemListsDTO dto = new TodoItemListsDTO();
        when(todoItemService.getTodoItemListIDs()).thenReturn(dto);
        assertEquals(dto, todoItemController.getListIDs());
    }

    @Test
    void testGetAllTodoItems() {
        List<TodoItemsDTO> list = Collections.singletonList(new TodoItemsDTO());
        when(todoItemService.getTodoItemLists()).thenReturn(list);
        assertEquals(list, todoItemController.getAllTodoItems());
    }

    @Test
    void testNewTodoItem() {
        when(todoItemService.saveTodoItem(sampleItem)).thenReturn(sampleItem);
        ResponseEntity<TodoItem> response = todoItemController.newTodoItem(sampleItem);
        assertEquals(sampleItem, response.getBody());
    }

    @Test
    void testEditTodoItem() {
        when(todoItemService.editTodoItem(sampleItem)).thenReturn(sampleItem);
        ResponseEntity<TodoItem> response = todoItemController.editTodoItem(sampleItem);
        assertEquals(sampleItem, response.getBody());
    }

    @Test
    void testDeleteTodoItem_Success() {
        when(todoItemService.deleteTodoItem(1L)).thenReturn(true);
        ResponseEntity<Boolean> response = todoItemController.deleteTodoItem(1L);
        assertEquals(true, response.getBody());
    }

    @Test
    void testDeleteTodoItem_NotFound() {
        when(todoItemService.deleteTodoItem(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> todoItemController.deleteTodoItem(1L));
    }

    @Test
    void testChangeDoneState_Success() {
        when(todoItemService.changeDoneStateForTodoItem(1L)).thenReturn(sampleItem);
        ResponseEntity<TodoItem> response = todoItemController.changeDoneState(1L);
        assertEquals(sampleItem, response.getBody());
    }

    @Test
    void testChangeDoneState_NotFound() {
        when(todoItemService.changeDoneStateForTodoItem(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> todoItemController.changeDoneState(1L));
    }
}
