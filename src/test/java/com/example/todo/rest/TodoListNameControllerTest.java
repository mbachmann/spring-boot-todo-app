package com.example.todo.rest;

import com.example.todo.TodoApplication;
import com.example.todo.dto.TodoItemsDTO;
import com.example.todo.dto.TodoListNameDTO;
import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoListNameRepository;
import com.example.todo.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes= TodoApplication.class)
@ExtendWith(MockitoExtension.class)
class TodoListNameControllerTest {

    @Mock
    private TodoListNameRepository todoListNameRepository;

    @Mock
    private TodoItemService todoItemService;

    @InjectMocks
    private TodoListNameController todoListNameController;

    private UUID listId;
    private TodoListName todoListName;
    private TodoItemsDTO todoItemsDTO;

    @BeforeEach
    void setUp() {
        listId = UUID.randomUUID();
        todoListName = new TodoListName();
        todoListName.setId(listId);
        todoListName.setName("Sample List");


        todoItemsDTO = new TodoItemsDTO(1, listId, Instant.now(), Instant.now(), "Sample List", null);
    }

    @Test
    void getAllTodoListNames_ReturnsListOfTodoListNameDTO() {
        when(todoItemService.getTodoItemLists()).thenReturn(Arrays.asList(todoItemsDTO));

        List<TodoListNameDTO> result = todoListNameController.getAllTodoListNames();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Sample List", result.get(0).getListName());
    }

    @Test
    void getTodoListNameById_Found_ReturnsTodoListNameDTO() {
        when(todoItemService.getTodoItemList(listId)).thenReturn(todoItemsDTO);

        ResponseEntity<TodoListNameDTO> response = todoListNameController.getTodoListNameById(listId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Sample List", response.getBody().getListName());
    }

    @Test
    void getTodoListNameById_NotFound_ReturnsNotFound() {
        when(todoItemService.getTodoItemList(listId)).thenReturn(null);

        ResponseEntity<TodoListNameDTO> response = todoListNameController.getTodoListNameById(listId);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createTodoListName_Success_ReturnsSavedTodoListName() {
        when(todoListNameRepository.save(todoListName)).thenReturn(todoListName);

        TodoListName result = todoListNameController.createTodoListName(todoListName);

        assertNotNull(result);
        assertEquals("Sample List", result.getName());
    }

    @Test
    void updateTodoListName_Found_UpdatesAndReturnsTodoListName() {
        TodoListName updatedDetails = new TodoListName();
        updatedDetails.setName("Updated List");

        when(todoListNameRepository.findById(listId)).thenReturn(Optional.of(todoListName));
        when(todoListNameRepository.save(any(TodoListName.class))).thenReturn(todoListName);

        ResponseEntity<TodoListName> response = todoListNameController.updateTodoListName(listId, updatedDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated List", response.getBody().getName());
    }

    @Test
    void updateTodoListName_NotFound_ReturnsNotFound() {
        when(todoListNameRepository.findById(listId)).thenReturn(Optional.empty());

        ResponseEntity<TodoListName> response = todoListNameController.updateTodoListName(listId, new TodoListName());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteTodoListName_Found_DeletesAndReturnsNoContent() {
        when(todoListNameRepository.findById(listId)).thenReturn(Optional.of(todoListName));
        doNothing().when(todoItemService).deleteByListId(listId);
        doNothing().when(todoListNameRepository).delete(todoListName);

        ResponseEntity<Void> response = todoListNameController.deleteTodoListName(listId);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteTodoListName_NotFound_ReturnsNotFound() {
        when(todoListNameRepository.findById(listId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = todoListNameController.deleteTodoListName(listId);

        assertEquals(404, response.getStatusCodeValue());
    }
}
